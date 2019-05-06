package com.ayratis.abstractapp.di

import com.ayratis.abstractapp.di.module.AppModule
import com.ayratis.abstractapp.di.module.DataBaseModule
import com.ayratis.abstractapp.di.module.NetModule
import com.ayratis.abstractapp.di.module.ViewModelModule
import com.ayratis.abstractapp.ui.main._activity.MainActivity
import com.ayratis.abstractapp.ui.main.dashoard.DashboardFragment
import com.ayratis.abstractapp.ui.main.home.HomeFragment
import com.ayratis.abstractapp.ui.main.list.ListFragment
import com.ayratis.abstractapp.ui.main.notifications.NotificationsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetModule::class,
    DataBaseModule::class,
    ViewModelModule::class
])

interface AppComponent {
    fun inject(appActivity: MainActivity)
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: ListFragment)
}
