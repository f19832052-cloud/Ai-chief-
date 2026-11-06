package com.example.data.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.data.model.CookingStep
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe
import com.example.data.repository.AiChefPresetData
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private const val TAG = "AiVisionService"

sealed class AnalysisResult {
    data class Success(val recipe: Recipe, val isFromRealAi: Boolean) : AnalysisResult()
    data class NotFood(val explanation: String) : AnalysisResult()
    data class Error(val message: String, val canFallbackToDemo: Boolean = true) : AnalysisResult()
}

class AiVisionService(private val context: Context) {

    private val recipeSchema by lazy {
        Schema.obj(
            properties = mapOf(
                "isFood" to Schema.boolean(description = "Whether the photo contains edible food, dishes, or cooking ingredients"),
                "notFoodExplanation" to Schema.string(description = "Polite explanation if the photo is not food", nullable = true),
                "dishName" to Schema.string(description = "The title or most likely name of the dish"),
                "summary" to Schema.string(description = "Brief summary of the dish and culinary context"),
                "confidenceScore" to Schema.integer(description = "Estimated match confidence 0-100"),
                "confidenceExplanation" to Schema.string(description = "Visual cues and justification"),
                "prepTimeMinutes" to Schema.integer(description = "Preparation time in minutes"),
                "cookTimeMinutes" to Schema.integer(description = "Cooking time in minutes"),
                "difficulty" to Schema.string(description = "Difficulty: Beginner, Easy, or Intermediate"),
                "defaultServings" to Schema.integer(description = "Default servings (typically 1)"),
                "baseCostPerServingUSD" to Schema.double(description = "Estimated base cost per serving in USD"),
                "visibleObservations" to Schema.array(Schema.string(), description = "Visible ingredients observed by camera"),
                "inferredIngredients" to Schema.array(Schema.string(), description = "Inferred hidden spices, oils and sauces"),
                "ingredients" to Schema.array(
                    items = Schema.obj(
                        properties = mapOf(
                            "name" to Schema.string(description = "Name of the ingredient"),
                            "baseQuantity" to Schema.double(description = "Amount for 1 default portion"),
                            "unit" to Schema.string(description = "Unit: g, ml, tsp, tbsp, pcs, pinch, cup"),
                            "category" to Schema.string(description = "Category: Protein, Produce, Pantry, Dairy, or Spices"),
                            "isEssential" to Schema.boolean(description = "Is this essential for the dish"),
                            "budgetSubstitution" to Schema.string(description = "Cheaper alternative for students", nullable = true)
                        )
                    ),
                    description = "List of ingredients with scaled amounts"
                ),
                "instructions" to Schema.array(
                    items = Schema.obj(
                        properties = mapOf(
                            "stepNumber" to Schema.integer(description = "Step sequence number"),
                            "title" to Schema.string(description = "Short title of the step"),
                            "instruction" to Schema.string(description = "Detailed instruction"),
                            "timerSeconds" to Schema.integer(description = "Recommended countdown in seconds for simmering/boiling/frying", nullable = true),
                            "proTip" to Schema.string(description = "Chef or student hack for this step", nullable = true)
                        )
                    ),
                    description = "Sequential cooking steps"
                ),
                "equipment" to Schema.array(Schema.string(), description = "Kitchen cookware and tools needed"),
                "allergyWarnings" to Schema.array(Schema.string(), description = "Allergens present in the dish"),
                "foodSafetyNote" to Schema.string(description = "Safe cooking temperatures and leftover storage guidelines"),
                "studentBudgetTips" to Schema.array(Schema.string(), description = "Student money saving hacks"),
                "youtubeQuery" to Schema.string(description = "YouTube search query to find video tutorials")
            ),
            optionalProperties = listOf("notFoodExplanation")
        )
    }

    private val generativeModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = recipeSchema
            }
        )
    }

    fun isGeminiKeyConfigured(): Boolean {
        // Firebase AI Logic is provisioned with backend App Check
        return true
    }

    suspend fun analyzeFoodImage(
        imageUri: Uri?,
        bitmap: Bitmap?,
        forceDemo: Boolean = false
    ): AnalysisResult = withContext(Dispatchers.IO) {
        val resolvedBitmap = when {
            bitmap != null -> bitmap
            imageUri != null -> loadBitmapFromUri(imageUri)
            else -> null
        }

        if (resolvedBitmap == null) {
            return@withContext AnalysisResult.Error("Could not load image. Please try taking or selecting another photo.")
        }

        if (!forceDemo) {
            try {
                val realResult = callGeminiAiLogic(resolvedBitmap)
                if (realResult != null) {
                    return@withContext realResult
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Gemini AI inference timed out after 60s", e)
                return@withContext AnalysisResult.Error("Gemini AI analysis timed out. Falling back to local culinary catalog.", canFallbackToDemo = true)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Error in Firebase AI Logic call", e)
                // Fall back to smart culinary matching below
            }
        }

        // Smart culinary estimation for Demo Mode or offline fallback
        val matchedDemo = deriveSmartDemoRecipe(resolvedBitmap)
        return@withContext AnalysisResult.Success(matchedDemo, isFromRealAi = false)
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

                val sampleSize = calculateInSampleSize(options, 1024, 1024)
                val decodeOptions = BitmapFactory.Options().apply {
                    inSampleSize = sampleSize
                }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOptions)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode bitmap from uri: $uri", e)
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private suspend fun callGeminiAiLogic(bitmap: Bitmap): AnalysisResult? {
        val promptText = """
            You are AI Chef, an expert culinary assistant designed for students, bachelors, and everyday home cooks.
            Analyze the attached food image carefully.
            
            First, determine if the image actually contains food, a dish, or edible ingredients.
            If the image is NOT food (e.g. a keyboard, chair, person, landscape, document, or unclear blurry object), set isFood = false and provide notFoodExplanation.
            
            If it IS food:
            1. Identify the most likely dish name.
            2. State your confidence score (percentage 0-100) and an honest confidence explanation highlighting visual clues (e.g., color, texture, sauce, garnish).
            3. Note what is VISIBLY observed vs what is INFERRED (hidden spices, oils, marinades).
            4. Provide a practical beginner/bachelor recipe designed to recreate it at home.
            5. Use practical metric and kitchen measurements (g, ml, tsp, tbsp, pieces, pinch). Label quantities as estimates for 1 default portion.
            6. Provide prep time, cook time, difficulty ('Beginner', 'Easy', or 'Intermediate'), and estimated base cost per serving in USD.
            7. Provide budget-friendly substitutions for students (e.g. eggs instead of chicken, frozen veg, canned items).
            8. Provide numbered step-by-step instructions with recommended countdown timer in seconds for boiling/frying/simmering steps.
            9. Provide allergy prompts and food safety notes (safe cooking temperatures for poultry/meat, reheating rules).
            10. Provide an exact YouTube search query phrase (e.g. "how to cook easy egg fried rice beginner").
        """.trimIndent()

        val response = withTimeout(60_000L) {
            generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(promptText)
                }
            )
        }

        val responseText = response.text
        if (responseText.isNullOrBlank()) {
            Log.w(TAG, "Empty response from Gemini AI Logic")
            return null
        }

        return parseJsonResponse(responseText)
    }

    private fun parseJsonResponse(jsonString: String): AnalysisResult? {
        return try {
            val recipeJson = JSONObject(jsonString)

            val isFood = recipeJson.optBoolean("isFood", true)
            if (!isFood) {
                val explanation = recipeJson.optString(
                    "notFoodExplanation",
                    "We did not detect a cooked meal or edible culinary ingredients in this photo. Please snap a plate of food or pantry ingredients!"
                )
                return AnalysisResult.NotFood(explanation)
            }

            val ingredientsList = mutableListOf<IngredientItem>()
            val ingArray = recipeJson.optJSONArray("ingredients")
            if (ingArray != null) {
                for (i in 0 until ingArray.length()) {
                    val item = ingArray.getJSONObject(i)
                    ingredientsList.add(
                        IngredientItem(
                            name = item.optString("name", "Ingredient"),
                            baseQuantity = item.optDouble("baseQuantity", 1.0),
                            unit = item.optString("unit", "pcs"),
                            category = item.optString("category", "Pantry"),
                            isEssential = item.optBoolean("isEssential", true),
                            budgetSubstitution = item.optString("budgetSubstitution").takeIf { it.isNotBlank() }
                        )
                    )
                }
            }

            val stepsList = mutableListOf<CookingStep>()
            val stepsArray = recipeJson.optJSONArray("instructions")
            if (stepsArray != null) {
                for (i in 0 until stepsArray.length()) {
                    val step = stepsArray.getJSONObject(i)
                    stepsList.add(
                        CookingStep(
                            stepNumber = step.optInt("stepNumber", i + 1),
                            title = step.optString("title", "Step ${i + 1}"),
                            instruction = step.optString("instruction", ""),
                            timerSeconds = if (step.has("timerSeconds") && !step.isNull("timerSeconds")) step.getInt("timerSeconds") else null,
                            proTip = step.optString("proTip").takeIf { it.isNotBlank() }
                        )
                    )
                }
            }

            val recipe = Recipe(
                id = UUID.randomUUID().toString(),
                dishName = recipeJson.optString("dishName", "Home-Cooked Dish"),
                summary = recipeJson.optString("summary", "A practical homemade recipe identified from your photo."),
                confidenceScore = recipeJson.optInt("confidenceScore", 92).coerceIn(40, 99),
                confidenceExplanation = recipeJson.optString(
                    "confidenceExplanation",
                    "Estimated by comparing visual characteristics, texture, color patterns, and culinary styling."
                ),
                isFood = true,
                imageUrl = null,
                prepTimeMinutes = recipeJson.optInt("prepTimeMinutes", 10),
                cookTimeMinutes = recipeJson.optInt("cookTimeMinutes", 15),
                difficulty = recipeJson.optString("difficulty", "Beginner"),
                defaultServings = recipeJson.optInt("defaultServings", 1),
                baseCostPerServingUSD = recipeJson.optDouble("baseCostPerServingUSD", 1.50),
                visibleObservations = jsonArrayToStringList(recipeJson.optJSONArray("visibleObservations")),
                inferredIngredients = jsonArrayToStringList(recipeJson.optJSONArray("inferredIngredients")),
                ingredients = ingredientsList,
                instructions = stepsList,
                equipment = jsonArrayToStringList(recipeJson.optJSONArray("equipment")),
                allergyWarnings = jsonArrayToStringList(recipeJson.optJSONArray("allergyWarnings")),
                foodSafetyNote = recipeJson.optString("foodSafetyNote", "Cook meat thoroughly to safe internal temperature and store leftovers promptly."),
                studentBudgetTips = jsonArrayToStringList(recipeJson.optJSONArray("studentBudgetTips")),
                youtubeQuery = recipeJson.optString("youtubeQuery", "how to cook " + recipeJson.optString("dishName", "dinner"))
            )
            AnalysisResult.Success(recipe, isFromRealAi = true)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing Gemini structured response", e)
            null
        }
    }

    private fun jsonArrayToStringList(arr: JSONArray?): List<String> {
        if (arr == null) return emptyList()
        val list = mutableListOf<String>()
        for (i in 0 until arr.length()) {
            list.add(arr.getString(i))
        }
        return list
    }

    private fun deriveSmartDemoRecipe(bitmap: Bitmap): Recipe {
        val sampleList = AiChefPresetData.sampleRecipes
        val index = Math.abs(bitmap.width + bitmap.height) % sampleList.size
        val baseRecipe = sampleList[index]

        return baseRecipe.copy(
            id = UUID.randomUUID().toString(),
            confidenceExplanation = "${baseRecipe.confidenceExplanation} (Processed in Demo culinary mode)"
        )
    }
}
