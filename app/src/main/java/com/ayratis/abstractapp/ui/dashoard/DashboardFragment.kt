package com.ayratis.abstractapp.ui.dashoard


import com.ayratis.abstractapp.App
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.ui.base.BaseMVVMFragment

class DashboardFragment : BaseMVVMFragment<com.ayratis.abstractapp.databinding.FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel
        get() = injectViewModel(viewModelFactory)

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun daggerInject() {
        App.getAppComponent().inject(this)
    }


}
