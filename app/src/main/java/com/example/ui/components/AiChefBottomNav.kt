package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.AppScreen

@Composable
fun AiChefBottomNav(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    NavigationBar(
        containerColor = WarmIvory,
        contentColor = Charcoal,
        windowInsets = WindowInsets.navigationBars,
        tonalElevation = 6.dp
    ) {
        val items = listOf(
            NavDestination(AppScreen.HOME, "Scan", Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt, "nav_home_button"),
            NavDestination(AppScreen.EXPLORE, "Explore", Icons.Filled.Explore, Icons.Outlined.Explore, "nav_explore_button"),
            NavDestination(AppScreen.BUDGET_MEALS, "Budget", Icons.Filled.Savings, Icons.Outlined.Savings, "nav_budget_button"),
            NavDestination(AppScreen.SAVED, "Saved", Icons.Filled.Bookmarks, Icons.Outlined.Bookmarks, "nav_saved_button"),
            NavDestination(AppScreen.PROFILE, "Kitchen", Icons.Filled.Person, Icons.Outlined.Person, "nav_kitchen_button")
        )

        items.forEach { dest ->
            val isSelected = currentScreen == dest.screen || (dest.screen == AppScreen.HOME && currentScreen == AppScreen.RECIPE_DETAIL)
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(dest.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) dest.filledIcon else dest.outlinedIcon,
                        contentDescription = dest.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = dest.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ForestGreen,
                    selectedTextColor = ForestGreen,
                    indicatorColor = FreshLime,
                    unselectedIconColor = CharcoalMuted,
                    unselectedTextColor = CharcoalMuted
                ),
                modifier = Modifier.testTag(dest.testTag)
            )
        }
    }
}

private data class NavDestination(
    val screen: AppScreen,
    val label: String,
    val filledIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val outlinedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
