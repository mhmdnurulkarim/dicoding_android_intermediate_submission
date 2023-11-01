package com.example.mystoryapps.view.add_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.*

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun addStories(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = storyRepository.addStories(file, description, lat, lon)

    fun getTokenUser() = storyRepository.getTokenUser().asLiveData(Dispatchers.IO)
}