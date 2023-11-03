package com.example.mystoryapps.view.list_story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapps.R
import com.example.mystoryapps.adapter.ListStoryAdapter
import com.example.mystoryapps.adapter.LoadingStateAdapter
import com.example.mystoryapps.databinding.ActivityListStoryBinding
import com.example.mystoryapps.network.Story
import com.example.mystoryapps.utils.Conts.EXTRA_USER
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.add_story.AddStoryActivity
import com.example.mystoryapps.view.detail_list_story.DetailStoryActivity
import com.example.mystoryapps.view.maps.MapsActivity
import com.example.mystoryapps.view.splash.SplashActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var itemDecoration: DividerItemDecoration
    private lateinit var mAdapter: ListStoryAdapter

    private val listViewModel: ListStoryViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onLoading()
        listViewModel.story.observe(this) { result ->
            onSuccess(result)
        }

        mLayoutManager = LinearLayoutManager(this)
        itemDecoration = DividerItemDecoration(this, mLayoutManager.orientation)
        mAdapter = ListStoryAdapter()

        binding.listStory.rvStory.apply {
            layoutManager = mLayoutManager
            addItemDecoration(itemDecoration)
            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    mAdapter.retry()
                }
            )
        }

        mAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val intentToDetail =
                    Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                intentToDetail.putExtra(EXTRA_USER, data.id)
                startActivity(intentToDetail)
            }
        })

        binding.apply {
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
                        MaterialAlertDialogBuilder(this@ListStoryActivity)
                            .setTitle(R.string.logout)
                            .setMessage(resources.getString(R.string.logout_message))
                            .setCancelable(true)
                            .setPositiveButton(R.string.Yes) { _, _ ->
                                onLoading()
                                startActivity(Intent(this@ListStoryActivity, SplashActivity::class.java))
                                finishAffinity()
                                listViewModel.logout()
                            }
                            .setNegativeButton(R.string.No, null)
                            .setIcon(R.drawable.ic_logout)
                            .show()
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

    private fun onSuccess(data: PagingData<Story>) {
        mAdapter.submitData(lifecycle, data)
        binding.listStory.apply {
            rvStory.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}