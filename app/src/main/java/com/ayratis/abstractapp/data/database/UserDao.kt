package com.ayratis.abstractapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayratis.abstractapp.entity.User
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE id > :id LIMIT :pageSize")
    fun getUsersFromId(id: Long, pageSize: Int): Single<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>): Single<List<Long>>
}