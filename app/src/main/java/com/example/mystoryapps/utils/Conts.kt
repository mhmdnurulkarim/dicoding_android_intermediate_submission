package com.example.mystoryapps.utils

import android.Manifest

object Conts {
    //Api service
    const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    //SplashActivity
    const val TIME_SPLASH = 3000L

    //List dan Detail Story
    const val EXTRA_USER = "EXTRA_USER"

    //Custom Email EditText
    const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+.-]+\$"

    //CameraX
    const val CAMERAX_RESULT = 200

    //Camera Utils
    const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

    //AddStory
    const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
}