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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Currency
import com.example.data.repository.AiChefPresetData
import com.example.ui.components.AiChefTopBar
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.SoftSageLight
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.AiChefViewModel

@Composable
fun BudgetMealsScreen(
    viewModel: AiChefViewModel,
    modifier: Modifier = Modifier
) {
    val preferences by viewModel.userPreferences.collectAsState()
    val currency = preferences.selectedCurrency

    // Filter budget-friendly dishes sorted by lowest cost
    val budgetRecipes = AiChefPresetData.sampleRecipes.sortedBy { it.baseCostPerServingUSD }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        AiChefTopBar(
            title = "Student Budget Meals",
            subtitle = "Save 60% on food"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Currency Switcher Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Active Currency: ${currency.name} (${currency.symbol})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = FreshLime
                                )
                                Text(
                                    text = "Prices calibrated for student grocery budgets",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            // Quick currency toggles
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Currency.values().forEach { cur ->
                                    val isSelected = cur == currency
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) FreshLime else Color.White.copy(alpha = 0.15f))
                                            .clickable { viewModel.updateCurrency(cur) }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = cur.symbol,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) ForestGreen else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bachelor Essential Pantry Guide
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftSageBorder, RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(FreshLime),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "The $10 / ৳1,000 Bachelor Pantry",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = ForestGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val pantryItems = listOf(
                            "Eggs (Protein MVP: 12 meals for pennies)",
                            "Red Lentils / Masoor Dal (Cooks in 10m, zero soaking)",
                            "Rice (Bulk 5kg bag cuts starch cost by 60%)",
                            "Onions & Garlic (Flavor backbone of 90% of meals)",
                            "Soy Sauce & Cooking Oil (Instant stir-fry glaze)",
                            "Potatoes (Boil in advance for curries & mash)"
                        )

                        pantryItems.forEach { item ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item,
                                    fontSize = 12.sp,
                                    color = Charcoal
                                )
                            }
                        }
                    }
                }
            }

            // Top Budget Dishes
            item {
                Text(
                    text = "Most Affordable Beginner Meals",
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ForestGreen
                )
            }

            items(budgetRecipes) { recipe ->
                val costFormatted = currency.format(recipe.baseCostPerServingUSD)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftSageBorder, RoundedCornerShape(16.dp))
                        .clickable { viewModel.viewRecipeDetails(recipe) }
                        .testTag("budget_recipe_${recipe.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = recipe.dishName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = ForestGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes} mins • ${recipe.ingredients.size} ingredients",
                                fontSize = 12.sp,
                                color = CharcoalMuted
                            )
                            if (recipe.studentBudgetTips.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "💡 ${recipe.studentBudgetTips.first()}",
                                    fontSize = 11.sp,
                                    color = Charcoal,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(FreshLime)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = costFormatted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
