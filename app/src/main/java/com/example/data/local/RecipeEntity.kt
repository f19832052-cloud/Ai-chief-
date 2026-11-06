package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CookingStep
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val dishName: String,
    val summary: String,
    val confidenceScore: Int,
    val confidenceExplanation: String,
    val isFood: Boolean,
    val imageUrl: String?,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: String,
    val defaultServings: Int,
    val baseCostPerServingUSD: Double,
    val visibleObservations: List<String>,
    val inferredIngredients: List<String>,
    val ingredients: List<IngredientItem>,
    val instructions: List<CookingStep>,
    val equipment: List<String>,
    val allergyWarnings: List<String>,
    val foodSafetyNote: String,
    val studentBudgetTips: List<String>,
    val youtubeQuery: String,
    val isFavorite: Boolean,
    val createdAt: Long
) {
    fun toDomainModel(): Recipe = Recipe(
        id = id,
        dishName = dishName,
        summary = summary,
        confidenceScore = confidenceScore,
        confidenceExplanation = confidenceExplanation,
        isFood = isFood,
        imageUrl = imageUrl,
        prepTimeMinutes = prepTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        difficulty = difficulty,
        defaultServings = defaultServings,
        baseCostPerServingUSD = baseCostPerServingUSD,
        visibleObservations = visibleObservations,
        inferredIngredients = inferredIngredients,
        ingredients = ingredients,
        instructions = instructions,
        equipment = equipment,
        allergyWarnings = allergyWarnings,
        foodSafetyNote = foodSafetyNote,
        studentBudgetTips = studentBudgetTips,
        youtubeQuery = youtubeQuery,
        isFavorite = isFavorite,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(recipe: Recipe): RecipeEntity = RecipeEntity(
            id = recipe.id,
            dishName = recipe.dishName,
            summary = recipe.summary,
            confidenceScore = recipe.confidenceScore,
            confidenceExplanation = recipe.confidenceExplanation,
            isFood = recipe.isFood,
            imageUrl = recipe.imageUrl,
            prepTimeMinutes = recipe.prepTimeMinutes,
            cookTimeMinutes = recipe.cookTimeMinutes,
            difficulty = recipe.difficulty,
            defaultServings = recipe.defaultServings,
            baseCostPerServingUSD = recipe.baseCostPerServingUSD,
            visibleObservations = recipe.visibleObservations,
            inferredIngredients = recipe.inferredIngredients,
            ingredients = recipe.ingredients,
            instructions = recipe.instructions,
            equipment = recipe.equipment,
            allergyWarnings = recipe.allergyWarnings,
            foodSafetyNote = recipe.foodSafetyNote,
            studentBudgetTips = recipe.studentBudgetTips,
            youtubeQuery = recipe.youtubeQuery,
            isFavorite = recipe.isFavorite,
            createdAt = recipe.createdAt
        )
    }
}
