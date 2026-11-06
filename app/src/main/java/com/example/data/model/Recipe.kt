package com.example.data.model

data class Recipe(
    val id: String,
    val dishName: String,
    val summary: String,
    val confidenceScore: Int, // e.g. 94 (0-100)
    val confidenceExplanation: String,
    val isFood: Boolean = true,
    val imageUrl: String? = null,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: String, // Beginner, Easy, Intermediate
    val defaultServings: Int = 1,
    val baseCostPerServingUSD: Double, // base USD cost for conversion
    val visibleObservations: List<String>,
    val inferredIngredients: List<String>,
    val ingredients: List<IngredientItem>,
    val instructions: List<CookingStep>,
    val equipment: List<String>,
    val allergyWarnings: List<String>,
    val foodSafetyNote: String,
    val studentBudgetTips: List<String>,
    val youtubeQuery: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class IngredientItem(
    val name: String,
    val baseQuantity: Double, // quantity for 1 default serving
    val unit: String, // g, ml, tsp, tbsp, pcs, pinch, cup
    val category: String, // Pantry, Produce, Protein, Dairy, Spices
    val isEssential: Boolean = true,
    val budgetSubstitution: String? = null
) {
    fun formattedQuantity(servings: Int, defaultServings: Int = 1): String {
        val multiplier = servings.toDouble() / defaultServings.coerceAtLeast(1)
        val calculated = baseQuantity * multiplier

        if (unit.lowercase() in listOf("pinch", "to taste", "as needed")) {
            return unit
        }

        val roundedStr = if (calculated % 1.0 == 0.0) {
            calculated.toInt().toString()
        } else if (calculated < 10) {
            String.format("%.1f", calculated)
        } else {
            Math.round(calculated).toString()
        }

        return "$roundedStr $unit"
    }
}

data class CookingStep(
    val stepNumber: Int,
    val title: String,
    val instruction: String,
    val timerSeconds: Int? = null,
    val proTip: String? = null
)

enum class Currency(
    val code: String,
    val symbol: String,
    val rateFromUSD: Double,
    val isPrefix: Boolean = true
) {
    BDT("BDT", "৳", 122.0, isPrefix = true),
    USD("USD", "$", 1.0, isPrefix = true),
    EUR("EUR", "€", 0.92, isPrefix = true),
    GBP("GBP", "£", 0.79, isPrefix = true),
    INR("INR", "₹", 86.5, isPrefix = true);

    fun format(amountUSD: Double): String {
        val converted = amountUSD * rateFromUSD
        val formattedNumber = if (code == "BDT" || code == "INR") {
            String.format("%.0f", converted)
        } else {
            String.format("%.2f", converted)
        }
        return if (isPrefix) "$symbol$formattedNumber" else "$formattedNumber $symbol"
    }
}

data class UserPreferences(
    val selectedCurrency: Currency = Currency.BDT,
    val skillLevel: String = "Beginner", // Beginner, Bachelor, Intermediate
    val dietaryRestriction: String = "None", // None, Halal, Vegetarian, Vegan, Gluten-Free
    val spiceTolerance: String = "Medium", // Mild, Medium, Hot
    val isDemoMode: Boolean = false
)
