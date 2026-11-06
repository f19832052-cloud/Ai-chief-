package com.example.data.repository

import com.example.data.local.RecipeDao
import com.example.data.local.RecipeEntity
import com.example.data.model.Currency
import com.example.data.model.Recipe
import com.example.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class AiChefRepository(private val recipeDao: RecipeDao) {

    private val _userPreferences = MutableStateFlow(
        UserPreferences(
            selectedCurrency = Currency.BDT, // Default Bangladesh Taka as requested in brief
            skillLevel = "Beginner",
            dietaryRestriction = "None",
            spiceTolerance = "Medium",
            isDemoMode = false
        )
    )
    val userPreferences: StateFlow<UserPreferences> = _userPreferences.asStateFlow()

    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes().map { entities ->
        entities.map { it.toDomainModel() }
    }

    val favoriteRecipes: Flow<List<Recipe>> = recipeDao.getFavoriteRecipes().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun getRecipeById(id: String): Recipe? {
        // First check database
        val fromDb = recipeDao.getRecipeById(id)?.toDomainModel()
        if (fromDb != null) return fromDb
        // Then check preset catalog
        return AiChefPresetData.sampleRecipes.find { it.id == id }
    }

    fun searchRecipes(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun saveRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(RecipeEntity.fromDomain(recipe))
    }

    suspend fun toggleFavorite(id: String, currentFavorite: Boolean) {
        val newFav = !currentFavorite
        recipeDao.updateFavorite(id, newFav)
    }

    suspend fun deleteRecipe(id: String) {
        recipeDao.deleteRecipeById(id)
    }

    suspend fun clearHistory() {
        recipeDao.clearHistory()
    }

    fun updateCurrency(currency: Currency) {
        _userPreferences.value = _userPreferences.value.copy(selectedCurrency = currency)
    }

    fun updateSkillLevel(skillLevel: String) {
        _userPreferences.value = _userPreferences.value.copy(skillLevel = skillLevel)
    }

    fun updateDietaryRestriction(restriction: String) {
        _userPreferences.value = _userPreferences.value.copy(dietaryRestriction = restriction)
    }

    fun updateSpiceTolerance(tolerance: String) {
        _userPreferences.value = _userPreferences.value.copy(spiceTolerance = tolerance)
    }

    fun toggleDemoMode(enabled: Boolean) {
        _userPreferences.value = _userPreferences.value.copy(isDemoMode = enabled)
    }
}
