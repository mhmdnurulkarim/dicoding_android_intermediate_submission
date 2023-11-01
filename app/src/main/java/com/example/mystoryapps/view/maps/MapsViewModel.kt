package com.example.mystoryapps.view.maps

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getAllStoriesWithLocation() = storyRepository.getAllStoriesWithLocation()
}