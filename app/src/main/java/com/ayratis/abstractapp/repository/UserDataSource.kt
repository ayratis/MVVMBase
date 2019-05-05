package com.ayratis.abstractapp.repository

import com.ayratis.abstractapp.entity.User
import io.reactivex.Flowable

interface UserDataSource {
    fun getUsers(userId: Long, perPage: Int): Flowable<List<User>>
}