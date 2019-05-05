package com.ayratis.abstractapp.repository

import android.util.Log
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.utils.subOnIoObsOnMain
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserDataSource {

    private val TAG = "UserRepository"

    override fun getUsers(userId: Long, perPage: Int): Flowable<List<User>> {

        val remote = userRemoteDataSource
            .getUsers(userId, perPage)
            .doOnNext {
                it.forEach { user -> Log.d(TAG, "remote " + user.id.toString()) }
                Log.d(TAG, "SAVING IN DB")
                saveUsers(it)
            }

        val local = userLocalDataSource
            .getUsers(userId, perPage)
            .doOnError {
                Log.d(TAG, "local error " + it.message)
            }
            .doOnNext {
                if(it.isEmpty()) Log.d(TAG, "local userList is empty")
                it.forEach { user -> Log.d(TAG, "local " + user.id.toString()) }
            }
            .flatMap {
                if(it.isEmpty()) {
                    remote
                } else {
                    Flowable.just(it)
                }
            }


        return local
    }

    private fun saveUsers(users: List<User>) {
        userLocalDataSource
            .addUsers(users)
            .subscribe()
    }


}