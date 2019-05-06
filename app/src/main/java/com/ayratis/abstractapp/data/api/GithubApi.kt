package com.ayratis.abstractapp.data.api

import com.ayratis.abstractapp.entity.User
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("/users")
    fun getUsers(@Query("since") userId: Long, @Query("per_page") perPage: Int): Flowable<List<User>>

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}