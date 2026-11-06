package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CookingStep
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe
import com.example.ui.components.AiChefTopBar
import com.example.ui.components.CookingTimerBanner
import com.example.ui.components.ServingCalculatorBar
import com.example.ui.components.StudentBudgetCard
import com.example.ui.theme.AllergenAmber
import com.example.ui.theme.AllergenAmberContainer
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.SafeTempBlue
import com.example.ui.theme.SafeTempBlueContainer
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.SoftSageLight
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.AiChefViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    viewModel: AiChefViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val servings by viewModel.servingsCount.collectAsState()
    val preferences by viewModel.userPreferences.collectAsState()
    val checkedIngredients by viewModel.checkedIngredients.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    BackHandler {
        viewModel.handleBackNavigation()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AiChefTopBar(
                title = recipe.dishName,
                showBackButton = true,
                onBackClick = { viewModel.handleBackNavigation() },
                recipeToShare = recipe,
                isFavorite = recipe.isFavorite,
                onFavoriteToggle = { viewModel.toggleFavorite(recipe) }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header & Confidence Indicator
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SoftSageBorder, RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(FreshLime)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${recipe.confidenceScore}% Match • High Confidence",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = ForestGreen,
                                        modifier = Modifier.testTag("confidence_badge")
                                    )
                                }

                                Text(
                                    text = recipe.difficulty,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = ForestGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = recipe.dishName,
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                lineHeight = 30.sp,
                                color = ForestGreen
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = recipe.summary,
                                fontSize = 13.sp,
                                color = Charcoal,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Key Cooking Metrics: Prep, Cook, Servings
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SoftSageLight)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MetricItem(icon = Icons.Default.AccessTime, label = "Prep", value = "${recipe.prepTimeMinutes}m")
                                MetricItem(icon = Icons.Default.LocalFireDepartment, label = "Cook", value = "${recipe.cookTimeMinutes}m")
                                MetricItem(icon = Icons.Default.RestaurantMenu, label = "Base", value = "${recipe.defaultServings} serving")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "💡 AI Identification Notes: ${recipe.confidenceExplanation}",
                                fontSize = 12.sp,
                                color = CharcoalMuted,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Observations vs Inferred Ingredients (Safety & Accuracy Rule)
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
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Visual Observations vs Inferred Spices",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ForestGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Camera Observed:",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Charcoal
                            )
                            recipe.visibleObservations.forEach { obs ->
                                Text(
                                    text = "• $obs",
                                    fontSize = 12.sp,
                                    color = CharcoalMuted,
                                    modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Inferred Hidden Flavors & Oils:",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Charcoal
                            )
                            recipe.inferredIngredients.forEach { inf ->
                                Text(
                                    text = "• $inf",
                                    fontSize = 12.sp,
                                    color = CharcoalMuted,
                                    modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Serving Calculator Bar
                item {
                    ServingCalculatorBar(
                        servings = servings,
                        onServingsChange = { viewModel.setServings(it) }
                    )
                }

                // Student Budget Mode Card
                item {
                    StudentBudgetCard(
                        baseCostUSD = recipe.baseCostPerServingUSD,
                        servings = servings,
                        currentCurrency = preferences.selectedCurrency,
                        onCurrencyChange = { viewModel.updateCurrency(it) },
                        budgetTips = recipe.studentBudgetTips
                    )
                }

                // Food Safety and Allergen Alerts
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (recipe.allergyWarnings.isNotEmpty()) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = AllergenAmberContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Allergy notice",
                                        tint = AllergenAmber,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Allergy Alert: ${recipe.allergyWarnings.joinToString(" ")}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Charcoal
                                    )
                                }
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SafeTempBlueContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Food safety",
                                    tint = SafeTempBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = recipe.foodSafetyNote,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Charcoal
                                )
                            }
                        }
                    }
                }

                // Ingredients List with Checkboxes and Scaled Quantities
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SoftSageBorder, RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ingredients (${servings}x portion)",
                                    fontFamily = PlayfairDisplayFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = ForestGreen
                                )
                                Text(
                                    text = "Tap to check pantry",
                                    fontSize = 11.sp,
                                    color = CharcoalMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            recipe.ingredients.forEach { item ->
                                val isChecked = checkedIngredients.contains(item.name)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.toggleIngredientChecked(item.name) }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { viewModel.toggleIngredientChecked(item.name) },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = ForestGreen,
                                            checkmarkColor = FreshLime
                                        ),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isChecked) CharcoalMuted else Charcoal,
                                            textDecoration = if (isChecked) TextDecoration.LineThrough else null
                                        )
                                        if (item.budgetSubstitution != null) {
                                            Text(
                                                text = "Swap: ${item.budgetSubstitution}",
                                                fontSize = 11.sp,
                                                color = ForestGreen,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    }
                                    Text(
                                        text = item.formattedQuantity(servings, recipe.defaultServings),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChecked) CharcoalMuted else ForestGreen
                                    )
                                }
                            }
                        }
                    }
                }

                // Step-by-Step Cooking Guide with Interactive Timers
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SoftSageBorder, RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Step-by-Step Cooking Guide",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = ForestGreen
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            recipe.instructions.forEach { step ->
                                StepCard(
                                    step = step,
                                    onStartTimer = { seconds ->
                                        viewModel.startTimer(step.stepNumber, seconds)
                                    }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }

                // Kitchen Equipment Needed
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
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Kitchen Equipment Needed",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ForestGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                recipe.equipment.forEach { eq ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SoftSage)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = eq,
                                            fontSize = 12.sp,
                                            color = Charcoal,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // YouTube Cooking Tutorial Link (Real verified search query!)
                item {
                    Button(
                        onClick = {
                            val encodedQuery = Uri.encode(recipe.youtubeQuery)
                            val youtubeUrl = "https://www.youtube.com/results?search_query=$encodedQuery"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = FreshLime
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("youtube_tutorial_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = null,
                            tint = FreshLime,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Watch Tutorial Videos on YouTube",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Active countdown timer floating at bottom
        CookingTimerBanner(
            timerState = timerState,
            onPause = { viewModel.pauseTimer() },
            onResume = { viewModel.resumeTimer() },
            onClose = { viewModel.resetTimer() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun MetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreen
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = CharcoalMuted
        )
    }
}

@Composable
private fun StepCard(
    step: CookingStep,
    onStartTimer: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SoftSageLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(ForestGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.stepNumber}",
                            color = FreshLime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = step.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ForestGreen
                    )
                }

                if (step.timerSeconds != null && step.timerSeconds > 0) {
                    val minutes = step.timerSeconds / 60
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FreshLime)
                            .clickable { onStartTimer(step.timerSeconds) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("step_${step.stepNumber}_timer_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Start ${minutes}m Timer",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = step.instruction,
                fontSize = 13.sp,
                color = Charcoal,
                lineHeight = 18.sp
            )

            if (step.proTip != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 Chef Tip: ${step.proTip}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = ForestGreen
                )
            }
        }
    }
}
