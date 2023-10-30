package com.example.mystoryapps.view.list_story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapps.R
import com.example.mystoryapps.adapter.ListStoryAdapter
import com.example.mystoryapps.data.Result
import com.example.mystoryapps.data.response.Story
import com.example.mystoryapps.databinding.ActivityListStoryBinding
import com.example.mystoryapps.utils.Conts.EXTRA_USER
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.add_story.AddStoryActivity
import com.example.mystoryapps.view.detail_list_story.DetailStoryActivity
import com.example.mystoryapps.view.maps.MapsActivity
import com.example.mystoryapps.view.splash.SplashActivity

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var mAdapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val listViewModel: ListStoryViewModel by viewModels { factory }

        val mLayoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, mLayoutManager.orientation)
        mAdapter = ListStoryAdapter()

        onLoading()
        listViewModel.getTokenUser().observe(this@ListStoryActivity) { token ->
            listViewModel.getAllStories(token.toString())
                .observe(this@ListStoryActivity) { result ->
                    when (result) {
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

            mAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Story) {
                    val intentToDetail =
                        Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                    intentToDetail.putExtra(EXTRA_USER, data.id)
                    startActivity(intentToDetail)
                }
            })
        }

        binding.apply {
            listStory.rvStory.apply {
                layoutManager = mLayoutManager
                addItemDecoration(itemDecoration)
                adapter = mAdapter
            }

            fabAddStory.setOnClickListener {
                startActivity(Intent(this@ListStoryActivity, AddStoryActivity::class.java))
            }

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_maps -> {
                        startActivity(Intent(this@ListStoryActivity, MapsActivity::class.java))
                    }

                    R.id.menu_localization -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }

                    R.id.menu_logout -> {
                        onLoading()
                        startActivity(Intent(this@ListStoryActivity, SplashActivity::class.java))
                        finishAffinity()
                        listViewModel.logout()
                    }
                }
                true
            }
        }
    }

    private fun onLoading() {
        binding.listStory.apply {
            rvStory.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun onError(message: String) {
        binding.listStory.apply {
            rvStory.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
        Log.d("ListStoryActivity: ", message)
    }

    private fun onSuccess(story: List<Story>) {
        mAdapter.submitList(story)
        binding.listStory.apply {
            rvStory.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}