package com.example.mystoryapps.view.list_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getAllStories(token: String) = storyRepository.getAllStories(token)

    fun getTokenUser() = storyRepository.getTokenUser().asLiveData(Dispatchers.IO)

    fun logout() {
        viewModelScope.launch { storyRepository.logout() }
    }
}