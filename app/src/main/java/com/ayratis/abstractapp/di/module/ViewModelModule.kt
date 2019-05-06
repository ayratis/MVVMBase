package com.ayratis.abstractapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ayratis.abstractapp.arch.view_model.ViewModelFactory
import com.ayratis.abstractapp.arch.view_model.ViewModelKey
import com.ayratis.abstractapp.ui.main.dashoard.DashboardViewModel
import com.ayratis.abstractapp.ui.main.home.HomeViewModel
import com.ayratis.abstractapp.ui.main.list.ListViewModel
import com.ayratis.abstractapp.ui.main.notifications.NotificationsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap



@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    abstract fun bindNotificationsViewModel(viewModel: NotificationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(viewModel: ListViewModel): ViewModel

}
