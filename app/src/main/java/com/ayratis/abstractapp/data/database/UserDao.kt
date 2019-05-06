package com.ayratis.abstractapp.data.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayratis.abstractapp.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE id = :id")
    fun getUserById(id: Long): Flowable<User>

    @Query("SELECT * FROM Users WHERE id > :id LIMIT :pageSize")
    fun getUsersFromId(id: Long, pageSize: Int): Flowable<List<User>>

    @Query("SELECT * FROM Users WHERE id > :id LIMIT :pageSize")
    fun getSingleUsersFromId(id: Long, pageSize: Int): Single<List<User>>

    @Query("SELECT * FROM Users LIMIT :count OFFSET :offset")
    fun getUsersByRange(count: Long, offset: Int): Flowable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>): Completable

    @Query("DELETE FROM Users WHERE id = :id")
    fun deleteUser(id: Long): Completable

    @Query("DELETE FROM Users")
    fun deleteAllUsers()

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): DataSource.Factory<Int, User>
}