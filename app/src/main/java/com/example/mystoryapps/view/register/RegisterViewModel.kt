package com.example.mystoryapps.view.register

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.data.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        authRepository.userRegister(name, email, password)
}