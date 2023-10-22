package com.example.mystoryapps.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    suspend fun saveTokenUser(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getTokenUser(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        @Volatile
        private var mInstance: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return mInstance ?: synchronized(this) {
                val newInstance = UserPreference(dataStore)
                mInstance = newInstance
                newInstance
            }
        }

        private val TOKEN_KEY = stringPreferencesKey("token_data")
    }
}