package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Recipe
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.WarmIvory

@Composable
fun AiChefTopBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    recipeToShare: Recipe? = null,
    isFavorite: Boolean = false,
    onFavoriteToggle: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Surface(
        color = WarmIvory,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .testTag("topbar_back_button")
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ForestGreen
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (title.length > 20) 20.sp else 23.sp,
                        color = ForestGreen,
                        maxLines = 1
                    )
                    if (subtitle != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(FreshLime)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = subtitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen
                            )
                        }
                    }
                }
            }

            if (onFavoriteToggle != null) {
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .testTag("favorite_button")
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (isFavorite) "Remove from Saved" else "Save Recipe",
                        tint = if (isFavorite) ForestGreen else Color.Gray
                    )
                }
            }

            if (recipeToShare != null) {
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = { shareRecipe(context, recipeToShare) },
                    modifier = Modifier
                        .testTag("share_recipe_button")
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Recipe",
                        tint = ForestGreen
                    )
                }
            }
        }
    }
}

private fun shareRecipe(context: Context, recipe: Recipe) {
    val shareText = buildString {
        appendLine("🍳 ${recipe.dishName} — AI Chef Recipe")
        appendLine("⏱️ Prep: ${recipe.prepTimeMinutes}m | Cook: ${recipe.cookTimeMinutes}m | Difficulty: ${recipe.difficulty}")
        appendLine()
        appendLine("📝 Ingredients (${recipe.defaultServings} portion):")
        recipe.ingredients.forEach { ing ->
            appendLine("• ${ing.name} - ${ing.formattedQuantity(1, 1)}")
        }
        appendLine()
        appendLine("👨‍🍳 Method:")
        recipe.instructions.forEach { step ->
            appendLine("${step.stepNumber}. ${step.title}: ${step.instruction}")
        }
        appendLine()
        appendLine("💡 Student Tip: ${recipe.studentBudgetTips.firstOrNull() ?: "Batch cook for the week!"}")
        appendLine("Shared via AI Chef: See it. Cook it.")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, recipe.dishName)
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Recipe via"))
}
