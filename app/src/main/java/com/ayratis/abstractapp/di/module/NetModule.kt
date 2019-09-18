package com.ayratis.abstractapp.di.module

import com.ayratis.abstractapp.BuildConfig
import com.ayratis.abstractapp.data.api.GithubApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideGithubApi(okHttpClient: OkHttpClient): GithubApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_PATH)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}
