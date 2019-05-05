package com.ayratis.abstractapp.ui.notifications


import com.ayratis.abstractapp.App
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.ui.base.BaseMVVMFragment

class NotificationsFragment : BaseMVVMFragment<com.ayratis.abstractapp.databinding.FragmentNotificationsBinding, NotificationsViewModel>() {
    override val viewModel: NotificationsViewModel
        get() = injectViewModel(viewModelFactory)
    override val layoutId: Int
        get() = R.layout.fragment_notifications

    override fun daggerInject() {
        App.getAppComponent().inject(this)
    }


}
