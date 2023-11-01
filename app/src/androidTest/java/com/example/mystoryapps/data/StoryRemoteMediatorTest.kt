package com.example.mystoryapps.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mystoryapps.database.StoryDatabase
import com.example.mystoryapps.network.ApiService
import com.example.mystoryapps.network.DetailStoryResponse
import com.example.mystoryapps.network.GeneralResponse
import com.example.mystoryapps.network.GetAllStoryResponse
import com.example.mystoryapps.network.LoginResponse
import com.example.mystoryapps.network.Story
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, Story>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun getAllStories(page: Int, size: Int): GetAllStoryResponse {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "name $i",
                "description $i",
                "photoUrl $i",
                "createdAt $i",
            )
            items.add(story)
        }
        return GetAllStoryResponse(true, "Success",
            listStory = items.subList((page - 1) * size, (page - 1) * size + size))
    }

    override suspend fun register(name: String, email: String, password: String): GeneralResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): GeneralResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStoriesWithLocation(location: Int): GetAllStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailStories(id: String): DetailStoryResponse {
        TODO("Not yet implemented")
    }
}