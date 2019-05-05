package com.ayratis.abstractapp

import android.app.Application
import com.ayratis.abstractapp.di.*

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .dataBaseModule(DataBaseModule())
            .build()
    }

    companion object {
        private lateinit var appComponent: AppComponent
        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }

}