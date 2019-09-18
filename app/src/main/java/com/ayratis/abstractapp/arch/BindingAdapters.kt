package com.ayratis.abstractapp.arch

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
    @BindingAdapter("app:isVisible")
    fun isVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}