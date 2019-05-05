package com.ayratis.abstractapp.repository

import android.util.Log
import com.ayratis.abstractapp.data.database.UserDataBase
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.utils.subOnIoObsOnMain
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val userDataBase: UserDataBase) : UserDataSource {

    private val TAG = "UserLocalDataSource"

    override fun getUsers(userId: Long, perPage: Int): Flowable<List<User>> {

        Log.d(TAG, "getting users")
        return userDataBase
            .userDao()
            .getUsersFromId(userId, perPage)
            .subOnIoObsOnMain()
    }

    fun addUsers(users: List<User>): Completable {
        return userDataBase
            .userDao()
            .insertUsers(users)
            .subOnIoObsOnMain()
            .doOnComplete { Log.d("LocalRepo", "saved")}
    }

    fun getUser(id: Long): Flowable<List<User>> {
        return userDataBase
            .userDao()
            .getUserById(id+1)
            .subOnIoObsOnMain()
            .map { user -> arrayListOf(user) }
    }

//    fun getUsers(): Flowable<User> {
//        return userDataBase.userDao().getUsers().
//    }

}