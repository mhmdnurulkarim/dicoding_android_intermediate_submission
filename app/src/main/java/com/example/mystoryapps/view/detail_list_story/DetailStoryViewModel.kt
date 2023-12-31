package com.example.mystoryapps.view.detail_list_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.Dispatchers

class DetailStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getDetailStories(token: String, id: String) = storyRepository.getDetailStories(token, id)

    fun getTokenUser() = storyRepository.getTokenUser().asLiveData(Dispatchers.IO)
}