package com.example.mystoryapps.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {
    fun String.withDateFormat(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = format.parse(this) as Date
        return DateFormat.getDateInstance(DateFormat.FULL).format(date)
    }

    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}