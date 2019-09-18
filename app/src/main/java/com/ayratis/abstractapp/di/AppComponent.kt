package com.ayratis.abstractapp.di

import androidx.lifecycle.ViewModelProvider
import com.ayratis.abstractapp.di.module.AppModule
import com.ayratis.abstractapp.di.module.DataBaseModule
import com.ayratis.abstractapp.di.module.NetModule
import com.ayratis.abstractapp.di.module.ViewModelModule
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
    fun viewModelFactory(): ViewModelProvider.Factory
}
