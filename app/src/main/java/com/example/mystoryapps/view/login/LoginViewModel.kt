package com.example.mystoryapps.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun userLogin(email: String, password: String) = storyRepository.userLogin(email, password)

    fun saveTokenUser(tokenUser: String) {
        viewModelScope.launch {
            storyRepository.saveTokenUser(tokenUser)
        }
    }
}