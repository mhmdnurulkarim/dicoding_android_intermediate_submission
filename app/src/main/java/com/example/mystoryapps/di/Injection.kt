package com.example.mystoryapps.di

import android.content.Context
import com.example.mystoryapps.data.AuthRepository
import com.example.mystoryapps.data.StoryRepository
import com.example.mystoryapps.database.StoryDatabase
import com.example.mystoryapps.datastore.UserPreference
import com.example.mystoryapps.datastore.dataStore
import com.example.mystoryapps.network.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val dataStore = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService("")
        return AuthRepository.getInstance(apiService, dataStore)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val dataStore = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { dataStore.getTokenUser().first() }
        val apiService = ApiConfig.getApiService(token)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, dataStore, database)
    }
}