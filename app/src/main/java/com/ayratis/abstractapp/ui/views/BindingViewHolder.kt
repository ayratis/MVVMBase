package com.ayratis.abstractapp.ui.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<B: ViewDataBinding>(val binding: B): RecyclerView.ViewHolder(binding.root){

    companion object {
        fun <B: ViewDataBinding>create(parent: ViewGroup, container: Int): BindingViewHolder<B> {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: B = DataBindingUtil.inflate(layoutInflater, container, parent, false)
            return BindingViewHolder(binding)
        }
    }
}

