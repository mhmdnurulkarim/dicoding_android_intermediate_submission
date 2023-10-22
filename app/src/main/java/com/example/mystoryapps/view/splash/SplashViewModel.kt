package com.example.mystoryapps.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SplashViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getTokenUser() = storyRepository.getTokenUser().asLiveData(Dispatchers.IO)
}