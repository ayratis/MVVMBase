package com.ayratis.abstractapp.repository

import android.util.Log
import com.ayratis.abstractapp.data.database.UserDataBase
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.utils.subOnIoObsOnMain
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
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

    fun getUsersSingle(userId: Long, perPage: Int): Single<List<User>> {

        Log.d(TAG, "getting users")
        return userDataBase
            .userDao()
            .getSingleUsersFromId(userId, perPage)
            .subOnIoObsOnMain()
    }

    fun addUsers(users: List<User>): Completable {
        return userDataBase
            .userDao()
            .insertUsers(users)
            .doOnSubscribe { Log.d(TAG, "saving users") }
            .subOnIoObsOnMain()
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