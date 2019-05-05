package com.ayratis.abstractapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayratis.abstractapp.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserDataBase: RoomDatabase() {

    abstract fun userDao(): UserDao
}