package com.ayratis.abstractapp.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:glide")
    fun glide(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("app:isGone")
    fun isGone(view: View, isGone: Boolean) {
        view.visibility = if (isGone) View.GONE else View.VISIBLE
    }
}