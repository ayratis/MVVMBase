package com.ayratis.abstractapp.ui.notifications


import android.os.Bundle
import com.ayratis.abstractapp.App
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.arch.view_model.viewModelProvider
import com.ayratis.abstractapp.databinding.FragmentNotificationsBinding
import com.ayratis.abstractapp.ui._base.BaseBindingFragment

class NotificationsFragment : BaseBindingFragment<FragmentNotificationsBinding>() {
    override val layoutId = R.layout.fragment_notifications

    private val viewModel:NotificationsViewModel by viewModelProvider()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
    }

}
