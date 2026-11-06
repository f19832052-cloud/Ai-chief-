package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.AiChefApplication
import com.example.data.ai.AnalysisResult
import com.example.data.model.Currency
import com.example.data.model.Recipe
import com.example.data.model.UserPreferences
import com.example.data.repository.AiChefPresetData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    RECIPE_DETAIL,
    EXPLORE,
    BUDGET_MEALS,
    SAVED,
    PROFILE
}

data class TimerState(
    val stepNumber: Int = 0,
    val totalSeconds: Int = 0,
    val remainingSeconds: Int = 0,
    val isRunning: Boolean = false
)

class AiChefViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AiChefApplication
    private val repository = app.repository
    private val visionService = app.visionService

    val userPreferences: StateFlow<UserPreferences> = repository.userPreferences

    val savedRecipes: StateFlow<List<Recipe>> = repository.allRecipes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteRecipes: StateFlow<List<Recipe>> = repository.favoriteRecipes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenBackStack = mutableListOf<AppScreen>()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    private val _servingsCount = MutableStateFlow(1)
    val servingsCount: StateFlow<Int> = _servingsCount.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisError = MutableStateFlow<String?>(null)
    val analysisError: StateFlow<String?> = _analysisError.asStateFlow()

    private val _notFoodWarning = MutableStateFlow<String?>(null)
    val notFoodWarning: StateFlow<String?> = _notFoodWarning.asStateFlow()

    private val _previewImageUri = MutableStateFlow<Uri?>(null)
    val previewImageUri: StateFlow<Uri?> = _previewImageUri.asStateFlow()

    private val _previewBitmap = MutableStateFlow<Bitmap?>(null)
    val previewBitmap: StateFlow<Bitmap?> = _previewBitmap.asStateFlow()

    private val _isRealAiOnline = MutableStateFlow(visionService.isGeminiKeyConfigured())
    val isRealAiOnline: StateFlow<Boolean> = _isRealAiOnline.asStateFlow()

    private val _checkedIngredients = MutableStateFlow<Set<String>>(emptySet())
    val checkedIngredients: StateFlow<Set<String>> = _checkedIngredients.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Pre-seed history with 2 beginner preset recipes if empty for immediate exploratory delight
        viewModelScope.launch {
            if (savedRecipes.value.isEmpty()) {
                val samples = AiChefPresetData.sampleRecipes
                if (samples.isNotEmpty()) {
                    repository.saveRecipe(samples[0])
                    if (samples.size > 1) {
                        repository.saveRecipe(samples[1])
                    }
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBackNavigation(): Boolean {
        if (_screenBackStack.isNotEmpty()) {
            val previous = _screenBackStack.removeAt(_screenBackStack.lastIndex)
            _currentScreen.value = previous
            return true
        } else if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            return true
        }
        return false
    }

    fun setImageForAnalysis(uri: Uri?, bitmap: Bitmap?) {
        _previewImageUri.value = uri
        _previewBitmap.value = bitmap
        _analysisError.value = null
        _notFoodWarning.value = null
    }

    fun clearImagePreview() {
        _previewImageUri.value = null
        _previewBitmap.value = null
        _analysisError.value = null
        _notFoodWarning.value = null
    }

    fun analyzeCurrentPhoto() {
        val uri = _previewImageUri.value
        val bitmap = _previewBitmap.value
        if (uri == null && bitmap == null) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisError.value = null
            _notFoodWarning.value = null

            val result = visionService.analyzeFoodImage(
                imageUri = uri,
                bitmap = bitmap,
                forceDemo = userPreferences.value.isDemoMode
            )

            _isAnalyzing.value = false

            when (result) {
                is AnalysisResult.Success -> {
                    val recipe = result.recipe
                    // Save to local Room history automatically
                    repository.saveRecipe(recipe)
                    viewRecipeDetails(recipe)
                }
                is AnalysisResult.NotFood -> {
                    _notFoodWarning.value = result.explanation
                }
                is AnalysisResult.Error -> {
                    _analysisError.value = result.message
                }
            }
        }
    }

    fun viewRecipeDetails(recipe: Recipe) {
        _selectedRecipe.value = recipe
        _servingsCount.value = recipe.defaultServings
        _checkedIngredients.value = emptySet()
        resetTimer()
        navigateTo(AppScreen.RECIPE_DETAIL)
    }

    fun selectPresetSample(recipe: Recipe) {
        viewRecipeDetails(recipe)
    }

    fun setServings(count: Int) {
        _servingsCount.value = count.coerceIn(1, 20)
    }

    fun toggleIngredientChecked(ingredientName: String) {
        val current = _checkedIngredients.value.toMutableSet()
        if (current.contains(ingredientName)) {
            current.remove(ingredientName)
        } else {
            current.add(ingredientName)
        }
        _checkedIngredients.value = current
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe.id, recipe.isFavorite)
            val updated = recipe.copy(isFavorite = !recipe.isFavorite)
            if (_selectedRecipe.value?.id == recipe.id) {
                _selectedRecipe.value = updated
            }
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            repository.deleteRecipe(recipeId)
            if (_selectedRecipe.value?.id == recipeId) {
                handleBackNavigation()
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // Step Countdown Timer
    fun startTimer(stepNumber: Int, seconds: Int) {
        timerJob?.cancel()
        _timerState.value = TimerState(
            stepNumber = stepNumber,
            totalSeconds = seconds,
            remainingSeconds = seconds,
            isRunning = true
        )

        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000)
                val newRemaining = _timerState.value.remainingSeconds - 1
                _timerState.value = _timerState.value.copy(
                    remainingSeconds = newRemaining,
                    isRunning = newRemaining > 0
                )
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = _timerState.value.copy(isRunning = false)
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (_timerState.value.remainingSeconds > 0) {
            _timerState.value = _timerState.value.copy(isRunning = true)
            timerJob = viewModelScope.launch {
                while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                    delay(1000)
                    val newRemaining = _timerState.value.remainingSeconds - 1
                    _timerState.value = _timerState.value.copy(
                        remainingSeconds = newRemaining,
                        isRunning = newRemaining > 0
                    )
                }
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState()
    }

    // Preferences
    fun updateCurrency(currency: Currency) {
        repository.updateCurrency(currency)
    }

    fun updateSkillLevel(level: String) {
        repository.updateSkillLevel(level)
    }

    fun updateDietaryRestriction(restriction: String) {
        repository.updateDietaryRestriction(restriction)
    }

    fun updateSpiceTolerance(tolerance: String) {
        repository.updateSpiceTolerance(tolerance)
    }

    fun toggleDemoMode(enabled: Boolean) {
        repository.toggleDemoMode(enabled)
    }
}
