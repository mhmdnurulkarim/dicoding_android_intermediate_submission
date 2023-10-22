package com.example.mystoryapps.view.register

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        storyRepository.userRegister(name, email, password)
}