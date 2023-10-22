package com.example.mystoryapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapps.data.pref.UserPreference
import com.example.mystoryapps.data.response.GeneralResponse
import com.example.mystoryapps.data.response.LoginResponse
import com.example.mystoryapps.data.response.Story
import com.example.mystoryapps.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val dataStore: UserPreference
    ) {

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

    fun getAllStories(token: String): LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories("Bearer $token")
            emit(Result.Success(response.listStory))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    fun getDetailStories(token: String, id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStories("Bearer $token", id)
            emit(Result.Success(response.story))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    fun addStories(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Result<GeneralResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addNewStory("Bearer $token", file, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    suspend fun saveTokenUser(token: String) {
        dataStore.saveTokenUser(token)
    }

    fun getTokenUser() = dataStore.getTokenUser()

    suspend fun logout() {
        dataStore.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            dataStore: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, dataStore)
            }.also { instance = it }
    }
}