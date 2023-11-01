package com.example.mystoryapps.di

import android.content.Context
import com.example.mystoryapps.data.AuthRepository
import com.example.mystoryapps.datastore.UserPreference
import com.example.mystoryapps.datastore.dataStore
import com.example.mystoryapps.network.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object TestInjection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val dataStore = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { dataStore.getTokenUser().first() }
        val apiService = ApiConfig.getApiService(token)
        return AuthRepository.getInstance(apiService, dataStore)
    }
}