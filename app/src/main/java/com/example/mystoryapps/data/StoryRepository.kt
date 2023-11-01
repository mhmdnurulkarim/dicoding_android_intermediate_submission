package com.example.mystoryapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapps.database.StoryDatabase
import com.example.mystoryapps.datastore.UserPreference
import com.example.mystoryapps.network.GeneralResponse
import com.example.mystoryapps.network.LoginResponse
import com.example.mystoryapps.network.Story
import com.example.mystoryapps.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val dataStore: UserPreference,
    private val database: StoryDatabase
) {
    fun getAllStories(): LiveData<PagingData<Story>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getAllStoriesWithLocation(): LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStoriesWithLocation()
            emit(Result.Success(response.listStory))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    fun getDetailStories(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStories(id)
            emit(Result.Success(response.story))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    fun addStories(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<Result<GeneralResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addNewStory(file, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
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
            dataStore: UserPreference,
            database: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, dataStore, database)
            }.also { instance = it }
    }
}