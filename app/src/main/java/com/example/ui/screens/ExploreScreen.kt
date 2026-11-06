package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Recipe
import com.example.data.repository.AiChefPresetData
import com.example.ui.components.AiChefTopBar
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.AiChefViewModel

@Composable
fun ExploreScreen(
    viewModel: AiChefViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Bachelor Essentials", "15-Min Quick", "High Protein", "Under ৳150", "Vegetarian")

    val preferences by viewModel.userPreferences.collectAsState()
    val savedRecipes by viewModel.savedRecipes.collectAsState()

    // Combine preset recipes and user's saved recipes for comprehensive exploration
    val combinedCatalog = remember(savedRecipes) {
        val preset = AiChefPresetData.sampleRecipes
        val savedIds = preset.map { it.id }.toSet()
        val customOnly = savedRecipes.filter { it.id !in savedIds }
        preset + customOnly
    }

    val filteredRecipes = combinedCatalog.filter { recipe ->
        val matchesSearch = searchQuery.isBlank() ||
            recipe.dishName.contains(searchQuery, ignoreCase = true) ||
            recipe.summary.contains(searchQuery, ignoreCase = true) ||
            recipe.ingredients.any { it.name.contains(searchQuery, ignoreCase = true) }

        val matchesCategory = when (selectedCategory) {
            "All" -> true
            "Bachelor Essentials" -> recipe.prepTimeMinutes + recipe.cookTimeMinutes <= 20
            "15-Min Quick" -> recipe.cookTimeMinutes <= 12
            "High Protein" -> recipe.ingredients.any { it.category == "Protein" }
            "Under ৳150" -> (recipe.baseCostPerServingUSD * preferences.selectedCurrency.rateFromUSD) <= 180
            "Vegetarian" -> recipe.allergyWarnings.any { it.contains("vegan", ignoreCase = true) } ||
                recipe.dishName.contains("Dal", ignoreCase = true) ||
                recipe.dishName.contains("Tofu", ignoreCase = true) ||
                recipe.dishName.contains("Pasta", ignoreCase = true)
            else -> true
        }

        matchesSearch && matchesCategory
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        AiChefTopBar(
            title = "Explore Recipes",
            subtitle = "${filteredRecipes.size} recipes"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by dish, pasta, lentils, eggs...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForestGreen,
                        unfocusedBorderColor = SoftSageBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_input")
                )
            }

            // Categories Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) ForestGreen else Color.White)
                                .border(1.dp, if (isSelected) ForestGreen else SoftSageBorder, RoundedCornerShape(20.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) FreshLime else Charcoal
                            )
                        }
                    }
                }
            }

            // Recipe List
            items(filteredRecipes) { recipe ->
                ExploreRecipeCard(
                    recipe = recipe,
                    currencyFormattedCost = preferences.selectedCurrency.format(recipe.baseCostPerServingUSD),
                    isFavorite = recipe.isFavorite,
                    onFavoriteToggle = { viewModel.toggleFavorite(recipe) },
                    onClick = { viewModel.viewRecipeDetails(recipe) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ExploreRecipeCard(
    recipe: Recipe,
    currencyFormattedCost: String,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftSageBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("recipe_card_${recipe.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(FreshLime)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "$currencyFormattedCost / portion",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isFavorite) ForestGreen else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recipe.dishName,
                fontFamily = PlayfairDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = ForestGreen
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = recipe.summary,
                fontSize = 12.sp,
                color = CharcoalMuted,
                maxLines = 2,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = CharcoalMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes} min total",
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                }

                Text(
                    text = "${recipe.difficulty} • ${recipe.ingredients.size} items",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ForestGreen
                )
            }
        }
    }
}
