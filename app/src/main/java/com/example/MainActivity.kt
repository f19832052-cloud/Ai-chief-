package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.repository.AiChefPresetData
import com.example.ui.components.AiChefBottomNav
import com.example.ui.screens.BudgetMealsScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AiChefViewModel
import com.example.ui.viewmodel.AppScreen

import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: AiChefViewModel = viewModel()
                AiChefApp(viewModel)
            }
        }
    }
}

@Composable
fun AiChefApp(viewModel: AiChefViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedRecipe by viewModel.selectedRecipe.collectAsState()

    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.handleBackNavigation()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen != AppScreen.RECIPE_DETAIL) {
                AiChefBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            label = "screen_crossfade"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.RECIPE_DETAIL -> {
                    val recipe = selectedRecipe ?: AiChefPresetData.sampleRecipes.first()
                    RecipeDetailScreen(
                        recipe = recipe,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppScreen.EXPLORE -> ExploreScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.BUDGET_MEALS -> BudgetMealsScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.SAVED -> SavedScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.PROFILE -> ProfileScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
