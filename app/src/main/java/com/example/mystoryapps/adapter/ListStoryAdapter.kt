package com.example.mystoryapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mystoryapps.data.response.Story
import com.example.mystoryapps.databinding.ItemStoryBinding
import com.example.mystoryapps.utils.Helper.loadImage
import com.example.mystoryapps.utils.Helper.withDateFormat

class ListStoryAdapter:ListAdapter<Story, ListStoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPosition = getItem(position)
        holder.bind(dataPosition)
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(dataPosition)
        }
    }

    inner class ViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story){
            binding.apply {
                tvItemName.text = story.name
                tvItemDescription.text = story.description
                tvItemCreate.text = story.createdAt.withDateFormat()
                ivItemPhoto.loadImage(story.photoUrl)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}