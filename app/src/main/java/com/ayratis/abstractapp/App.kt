package com.ayratis.abstractapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ayratis.abstractapp.di.*
import com.ayratis.abstractapp.di.module.AppModule
import com.ayratis.abstractapp.di.module.DataBaseModule
import com.ayratis.abstractapp.di.module.NetModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initDagger()
        createNotificationChannel()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .dataBaseModule(DataBaseModule())
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "lc.deck.bkzenit.ANDROID"
        lateinit var appComponent: AppComponent
    }
}