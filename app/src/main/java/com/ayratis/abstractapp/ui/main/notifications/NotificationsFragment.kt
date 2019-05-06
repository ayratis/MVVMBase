package com.ayratis.abstractapp.ui.main.notifications


import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.di.injector
import com.ayratis.abstractapp.ui._base.BaseMVVMFragment

class NotificationsFragment : BaseMVVMFragment<com.ayratis.abstractapp.databinding.FragmentNotificationsBinding, NotificationsViewModel>() {
    override val viewModel: NotificationsViewModel
        get() = injectViewModel(viewModelFactory)
    override val layoutId: Int
        get() = R.layout.fragment_notifications

    override fun daggerInject() {
        injector().inject(this)
    }


}
