package com.example.mystoryapps.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.mystoryapps.databinding.ActivitySplashBinding
import com.example.mystoryapps.utils.Conts.TIME_SPLASH
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.list_story.ListStoryActivity
import com.example.mystoryapps.view.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val splashViewModel: SplashViewModel by viewModels{ factory }

        Handler(mainLooper).postDelayed({
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    Pair(binding.icLauncher,"ic_launcher")
                )

            splashViewModel.getTokenUser().observe(this@SplashActivity) { token ->
                if (token.isNullOrEmpty()) {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent, optionsCompat.toBundle())
                    finishAffinity()
                } else {
                    startActivity(Intent(this@SplashActivity, ListStoryActivity::class.java))
                    finishAffinity()
                }
            }
        }, TIME_SPLASH)
    }
}