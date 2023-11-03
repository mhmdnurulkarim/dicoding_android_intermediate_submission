package com.example.mystoryapps.view.list_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapps.data.StoryRepository
import com.example.mystoryapps.network.Story
import kotlinx.coroutines.launch

class ListStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    val story: LiveData<PagingData<Story>> =
        storyRepository.getAllStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch { storyRepository.logout() }
    }
}