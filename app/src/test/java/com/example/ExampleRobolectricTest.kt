package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Currency
import com.example.data.model.IngredientItem
import com.example.data.repository.AiChefPresetData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string resource matches AI Chef`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AI Chef", appName)
    }

    @Test
    fun `serving calculation correctly scales ingredient quantities`() {
        val item = IngredientItem(
            name = "Cooked rice",
            baseQuantity = 200.0,
            unit = "g",
            category = "Pantry",
            isEssential = true
        )
        // 1 serving
        assertEquals("200 g", item.formattedQuantity(1, 1))
        // 3 servings
        assertEquals("600 g", item.formattedQuantity(3, 1))
        // 4 servings
        assertEquals("800 g", item.formattedQuantity(4, 1))
    }

    @Test
    fun `bdt currency formats with symbol and conversion rate`() {
        val bdt = Currency.BDT
        val formatted = bdt.format(1.0)
        assertTrue(formatted.startsWith("৳"))
        assertTrue(formatted.contains("122"))
    }

    @Test
    fun `preset dishes contain student fried rice and dal tadka`() {
        val dishes = AiChefPresetData.sampleRecipes
        assertTrue(dishes.isNotEmpty())
        val friedRice = dishes.find { it.id == "preset_fried_rice" }
        assertNotNull(friedRice)
        assertEquals("Beginner", friedRice?.difficulty)
        assertTrue(friedRice?.instructions?.isNotEmpty() == true)
    }
}
