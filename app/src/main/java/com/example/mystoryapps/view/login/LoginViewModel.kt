package com.example.mystoryapps.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapps.data.AuthRepository
import com.example.mystoryapps.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun userLogin(email: String, password: String) = authRepository.userLogin(email, password)

    fun saveTokenUser(tokenUser: String) {
        viewModelScope.launch {
            authRepository.saveTokenUser(tokenUser)
        }
    }

    fun getTokenUser() = authRepository.getTokenUser().asLiveData(Dispatchers.IO)

}