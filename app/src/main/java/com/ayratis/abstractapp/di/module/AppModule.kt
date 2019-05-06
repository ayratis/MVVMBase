package com.ayratis.abstractapp.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application){

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return application.applicationContext.getSharedPreferences("sp", Context.MODE_PRIVATE)
    }

}
