package com.ayratis.abstractapp.repository

import com.ayratis.abstractapp.data.api.GithubApi
import com.ayratis.abstractapp.data.database.UserDao
import com.ayratis.abstractapp.entity.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val api: GithubApi,
    private val userDao: UserDao
) {

    fun getUsers(key: Long, count: Int): Single<List<User>> =
        api.getUsers(key, count)
            .flatMap { users ->
                userDao
                    .insertUsers(users)
                    .map { users }
            }
            .onErrorResumeNext { error ->
                if (error is IOException) {
                    userDao
                        .getUsersFromId(key, count)
                } else {
                    throw error
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}