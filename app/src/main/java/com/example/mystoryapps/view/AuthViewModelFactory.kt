package com.example.mystoryapps.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.data.AuthRepository
import com.example.mystoryapps.data.StoryRepository
import com.example.mystoryapps.di.Injection
import com.example.mystoryapps.di.TestInjection
import com.example.mystoryapps.view.add_story.AddStoryViewModel
import com.example.mystoryapps.view.detail_list_story.DetailStoryViewModel
import com.example.mystoryapps.view.list_story.ListStoryViewModel
import com.example.mystoryapps.view.login.LoginViewModel
import com.example.mystoryapps.view.maps.MapsViewModel
import com.example.mystoryapps.view.register.RegisterViewModel
import com.example.mystoryapps.view.splash.SplashViewModel

class AuthViewModelFactory private constructor(
    private val authRepository: AuthRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel class: " + modelClass.name)
    }
    companion object{
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(
                    TestInjection.provideAuthRepository(context)
                )
            }.also { instance = it }
    }
}