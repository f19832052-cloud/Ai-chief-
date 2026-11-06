package com.example

import android.app.Application
import com.example.data.ai.AiVisionService
import com.example.data.local.AiChefDatabase
import com.example.data.repository.AiChefRepository

class AiChefApplication : Application() {

    lateinit var database: AiChefDatabase
        private set

    lateinit var repository: AiChefRepository
        private set

    lateinit var visionService: AiVisionService
        private set

    override fun onCreate() {
        super.onCreate()
        database = AiChefDatabase.getDatabase(this)
        repository = AiChefRepository(database.recipeDao())
        visionService = AiVisionService(this)
    }
}
