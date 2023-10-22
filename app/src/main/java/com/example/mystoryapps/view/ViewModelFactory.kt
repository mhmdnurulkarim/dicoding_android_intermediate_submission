package com.example.mystoryapps.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.data.StoryRepository
import com.example.mystoryapps.di.Injection
import com.example.mystoryapps.view.add_story.AddStoryViewModel
import com.example.mystoryapps.view.detail_list_story.DetailStoryViewModel
import com.example.mystoryapps.view.list_story.ListStoryViewModel
import com.example.mystoryapps.view.login.LoginViewModel
import com.example.mystoryapps.view.register.RegisterViewModel
import com.example.mystoryapps.view.splash.SplashViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)){
            return SplashViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)){
            return ListStoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)){
            return DetailStoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}