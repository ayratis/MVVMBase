package com.ayratis.abstractapp.di

import android.app.Application
import androidx.room.Room
import com.ayratis.abstractapp.data.database.UserDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun porovideUserDataBase(application: Application): UserDataBase {
        return Room.databaseBuilder(application, UserDataBase::class.java, "Users.db").build()
    }
}