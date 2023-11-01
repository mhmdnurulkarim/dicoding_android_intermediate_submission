package com.example.mystoryapps.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.mystoryapps.network.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}