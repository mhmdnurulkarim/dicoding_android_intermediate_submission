package com.example.mystoryapps.di

import android.content.Context
import com.example.mystoryapps.data.StoryRepository
import com.example.mystoryapps.data.pref.UserPreference
import com.example.mystoryapps.data.pref.dataStore
import com.example.mystoryapps.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { dataStore.getTokenUser().first()}
        return StoryRepository.getInstance(apiService, dataStore)
    }
}