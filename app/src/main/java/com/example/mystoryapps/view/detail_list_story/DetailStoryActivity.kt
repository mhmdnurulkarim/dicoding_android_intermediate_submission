package com.example.mystoryapps.view.detail_list_story

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapps.R
import com.example.mystoryapps.data.Result
import com.example.mystoryapps.data.response.Story
import com.example.mystoryapps.databinding.ActivityDetailStoryBinding
import com.example.mystoryapps.utils.Conts.EXTRA_USER
import com.example.mystoryapps.utils.Helper.loadImage
import com.example.mystoryapps.utils.Helper.withDateFormat
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.list_story.ListStoryActivity

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val detailViewModel: DetailStoryViewModel by viewModels{ factory }

        val idUser = intent.getStringExtra(EXTRA_USER)
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, ListStoryActivity::class.java))
            finishAffinity()
        }

        onLoading()
        detailViewModel.getTokenUser().observe(this@DetailStoryActivity) {token ->
            detailViewModel.getDetailStories(token.toString(), idUser.toString()).observe(this@DetailStoryActivity) { result ->
                when(result) {
                    is Result.Loading -> {
                        onLoading()
                    }
                    is Result.Success -> {
                        onSuccess(result.data)
                    }
                    is Result.Error -> {
                        onError(result.error)
                    }
                }
            }
        }
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(data: Story){
        binding.apply {
            progressBar.visibility = View.GONE
            tvDetailName.text = getString(R.string.detail_name, data.name)
            tvDetailCreate.text = getString(R.string.detail_time, data.createdAt.withDateFormat())
            tvDetailDescription.text = getString(R.string.detail_description, data.description)
            ivDetailPhoto.loadImage(data.photoUrl)
        }
    }
}