package com.example.mystoryapps

import com.example.mystoryapps.network.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "name $i",
                "description $i",
                "photoUrl $i",
                "createdAt $i",
                )
            items.add(story)
        }
        return items
    }
}