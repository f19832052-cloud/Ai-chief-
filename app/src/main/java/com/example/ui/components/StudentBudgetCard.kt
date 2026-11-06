package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Currency
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.FreshLime
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBorder

@Composable
fun StudentBudgetCard(
    baseCostUSD: Double,
    servings: Int,
    currentCurrency: Currency,
    onCurrencyChange: (Currency) -> Unit,
    budgetTips: List<String>,
    modifier: Modifier = Modifier
) {
    val totalCostUSD = baseCostUSD * servings
    val perServingFormatted = currentCurrency.format(baseCostUSD)
    val totalFormatted = currentCurrency.format(totalCostUSD)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, SoftSageBorder, RoundedCornerShape(18.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(FreshLime),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Student Budget Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ForestGreen
                        )
                        Text(
                            text = "Affordable cooking estimates",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                    }
                }

                // Currency switcher chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Currency.values().forEach { cur ->
                        val isSelected = cur == currentCurrency
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ForestGreen else SoftSage)
                                .clickable { onCurrencyChange(cur) }
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cur.symbol,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) FreshLime else Charcoal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pricing metrics grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftSage)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Cost per portion",
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                    Text(
                        text = perServingFormatted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier.testTag("cost_per_portion_text")
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(SoftSageBorder)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total for $servings ${if (servings == 1) "meal" else "meals"}",
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                    Text(
                        text = totalFormatted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier.testTag("total_cost_text")
                    )
                }
            }

            if (budgetTips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "💡 Bachelor & Student Money Hacks:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                budgetTips.take(2).forEach { tip ->
                    Text(
                        text = "• $tip",
                        fontSize = 12.sp,
                        color = Charcoal,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
