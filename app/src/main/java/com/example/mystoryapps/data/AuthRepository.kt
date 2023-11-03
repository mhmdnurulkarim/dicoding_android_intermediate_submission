package com.example.mystoryapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapps.datastore.UserPreference
import com.example.mystoryapps.network.ApiService
import com.example.mystoryapps.network.GeneralResponse
import com.example.mystoryapps.network.LoginResponse

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val dataStore: UserPreference,
){
    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    fun userRegister(name: String, email: String, password: String): LiveData<Result<GeneralResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }
    suspend fun saveTokenUser(token: String) {
        dataStore.saveTokenUser(token)
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            dataStore: UserPreference,

        ): AuthRepository = instance ?: AuthRepository(apiService, dataStore)
    }
}