package com.example.eydstest.presentation.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.eydstest.R
import com.example.eydstest.presentation.ui.adapter.FavoriteGifAdapter

object Constant {
    const val BASE_URL = "https://api.giphy.com/v1/gifs/"
    const val ERROR = "Something went wrong. Please try again later"
    const val TAB_SEARCH = "SEARCH"
    const val TAB_FAVORITE = "FAVORITE"
    const val NO_DATA = "No data"
    const val HTTP_STATUS_SUCCESS = 200
}

@BindingAdapter("gifUrl")
fun setGifUrl(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .into(view)
}

@BindingAdapter("favoriteIcon")
fun setFavoriteIcon(view: ImageView, isFavorite: Boolean) {
    view.setImageResource(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite)
}