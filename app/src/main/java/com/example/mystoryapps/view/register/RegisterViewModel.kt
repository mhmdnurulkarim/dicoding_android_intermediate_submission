package com.example.mystoryapps.view.register

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.data.AuthRepository
import com.example.mystoryapps.data.StoryRepository

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        authRepository.userRegister(name, email, password)
}