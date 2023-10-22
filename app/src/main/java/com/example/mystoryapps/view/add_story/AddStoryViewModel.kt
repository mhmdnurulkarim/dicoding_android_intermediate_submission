package com.example.mystoryapps.view.add_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun addStories(token: String, file: MultipartBody.Part, description: RequestBody) = storyRepository.addStories(token, file, description)

    fun getTokenUser() = storyRepository.getTokenUser().asLiveData(Dispatchers.IO)
}