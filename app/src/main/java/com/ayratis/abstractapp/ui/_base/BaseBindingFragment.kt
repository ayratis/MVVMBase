package com.ayratis.abstractapp.ui._base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<B : ViewDataBinding> : BaseFragment() {

    protected lateinit var binding: B
    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.apply {
            lifecycleOwner = this@BaseBindingFragment
            executePendingBindings()
        }
        return binding.root
    }

}
