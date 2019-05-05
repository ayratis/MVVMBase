package com.ayratis.abstractapp.di

import com.ayratis.abstractapp.MainActivity
import com.ayratis.abstractapp.ui.dashoard.DashboardFragment
import com.ayratis.abstractapp.ui.home.HomeFragment
import com.ayratis.abstractapp.ui.list.ListFragment
import com.ayratis.abstractapp.ui.notifications.NotificationsFragment
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
