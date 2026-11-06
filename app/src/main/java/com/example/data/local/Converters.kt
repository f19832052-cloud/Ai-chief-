package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.CookingStep
import com.example.data.model.IngredientItem
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        if (list == null) return "[]"
        val array = JSONArray()
        list.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        val array = JSONArray(data)
        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }
        return list
    }

    @TypeConverter
    fun fromIngredientList(list: List<IngredientItem>?): String {
        if (list == null) return "[]"
        val array = JSONArray()
        list.forEach { item ->
            val obj = JSONObject()
            obj.put("name", item.name)
            obj.put("baseQuantity", item.baseQuantity)
            obj.put("unit", item.unit)
            obj.put("category", item.category)
            obj.put("isEssential", item.isEssential)
            obj.put("budgetSubstitution", item.budgetSubstitution ?: "")
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toIngredientList(data: String?): List<IngredientItem> {
        if (data.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<IngredientItem>()
        val array = JSONArray(data)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val sub = obj.optString("budgetSubstitution")
            list.add(
                IngredientItem(
                    name = obj.getString("name"),
                    baseQuantity = obj.getDouble("baseQuantity"),
                    unit = obj.getString("unit"),
                    category = obj.optString("category", "Pantry"),
                    isEssential = obj.optBoolean("isEssential", true),
                    budgetSubstitution = if (sub.isNotEmpty()) sub else null
                )
            )
        }
        return list
    }

    @TypeConverter
    fun fromCookingStepList(list: List<CookingStep>?): String {
        if (list == null) return "[]"
        val array = JSONArray()
        list.forEach { step ->
            val obj = JSONObject()
            obj.put("stepNumber", step.stepNumber)
            obj.put("title", step.title)
            obj.put("instruction", step.instruction)
            if (step.timerSeconds != null) {
                obj.put("timerSeconds", step.timerSeconds)
            }
            if (step.proTip != null) {
                obj.put("proTip", step.proTip)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toCookingStepList(data: String?): List<CookingStep> {
        if (data.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<CookingStep>()
        val array = JSONArray(data)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val timer = if (obj.has("timerSeconds") && !obj.isNull("timerSeconds")) {
                obj.getInt("timerSeconds")
            } else null
            val tip = if (obj.has("proTip") && !obj.isNull("proTip")) {
                obj.getString("proTip")
            } else null
            list.add(
                CookingStep(
                    stepNumber = obj.getInt("stepNumber"),
                    title = obj.getString("title"),
                    instruction = obj.getString("instruction"),
                    timerSeconds = timer,
                    proTip = tip
                )
            )
        }
        return list
    }
}
