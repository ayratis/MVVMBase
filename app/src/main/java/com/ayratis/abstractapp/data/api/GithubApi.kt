package com.ayratis.abstractapp.data.api

import com.ayratis.abstractapp.entity.User
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("/users")
    fun getUsers(
        @Query("since") userId: Long,
        @Query("per_page") perPage: Int
    ): Single<List<User>>
}