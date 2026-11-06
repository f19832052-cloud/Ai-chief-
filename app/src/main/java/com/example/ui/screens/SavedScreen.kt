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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.components.AiChefTopBar
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.DangerRed
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.AiChefViewModel

@Composable
fun SavedScreen(
    viewModel: AiChefViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Favorites, 1: History
    var searchQuery by remember { mutableStateOf("") }

    val savedRecipes by viewModel.savedRecipes.collectAsState()
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsState()
    val preferences by viewModel.userPreferences.collectAsState()

    val currentList = if (selectedTab == 0) favoriteRecipes else savedRecipes

    val filteredList = currentList.filter {
        searchQuery.isBlank() ||
            it.dishName.contains(searchQuery, ignoreCase = true) ||
            it.summary.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        AiChefTopBar(
            title = "Personal Kitchen",
            subtitle = "${filteredList.size} items"
        )

        // Tab Selector (Favorites vs Scanned History)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = WarmIvory,
            contentColor = ForestGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ForestGreen,
                    height = 3.dp
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (selectedTab == 0) ForestGreen else CharcoalMuted
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Favorites (${favoriteRecipes.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedTab == 0) ForestGreen else CharcoalMuted
                        )
                    }
                },
                modifier = Modifier.testTag("tab_favorites")
            )

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (selectedTab == 1) ForestGreen else CharcoalMuted
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "History (${savedRecipes.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedTab == 1) ForestGreen else CharcoalMuted
                        )
                    }
                },
                modifier = Modifier.testTag("tab_history")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search saved recipes...") },
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
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (filteredList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(SoftSage),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Default.Bookmark else Icons.Default.RestaurantMenu,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (selectedTab == 0) "No favorites saved yet" else "No scan history yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ForestGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedTab == 0) {
                                "Tap the bookmark icon on any recipe to save it for quick access."
                            } else {
                                "Snap a food photo from the Scan tab to begin logging your dishes!"
                            },
                            fontSize = 13.sp,
                            color = CharcoalMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                items(filteredList) { recipe ->
                    SavedRecipeItemCard(
                        recipe = recipe,
                        costFormatted = preferences.selectedCurrency.format(recipe.baseCostPerServingUSD),
                        onDelete = { viewModel.deleteRecipe(recipe.id) },
                        onClick = { viewModel.viewRecipeDetails(recipe) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SavedRecipeItemCard(
    recipe: Recipe,
    costFormatted: String,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftSageBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("saved_recipe_${recipe.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.dishName,
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = ForestGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes} mins • $costFormatted / serving",
                    fontSize = 12.sp,
                    color = CharcoalMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = recipe.summary,
                    fontSize = 12.sp,
                    color = Charcoal,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("delete_recipe_${recipe.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete recipe",
                    tint = DangerRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
