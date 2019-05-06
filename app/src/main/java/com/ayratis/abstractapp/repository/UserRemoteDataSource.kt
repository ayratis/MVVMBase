package com.ayratis.abstractapp.repository

import com.ayratis.abstractapp.data.api.GithubApi
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.utils.subOnIoObsOnMain
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(private val githubApi: GithubApi) : UserDataSource {

    override fun getUsers(userId: Long, perPage: Int): Flowable<List<User>> {
        return githubApi
            .getUsers(userId, perPage)
            .subOnIoObsOnMain()
    }
}