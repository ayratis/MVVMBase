package com.ayratis.abstractapp.utils

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

fun <T> Observable<T>.subOnIoObsOnMain(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subOnIoObsOnMain(): Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.subOnIoObsOnMain(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.subOnIoObsOnMain(): Completable {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

@SuppressLint("RestrictedApi")
fun <Key, Value> DataSource.Factory<Key, Value>.toLiveData(
    pageSize: Int,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): LiveData<PagedList<Value>> {
    return LivePagedListBuilder(this, PagedList.Config.Builder().setPageSize(pageSize).build())
        .setInitialLoadKey(initialLoadKey)
        .setBoundaryCallback(boundaryCallback)
        .setFetchExecutor(fetchExecutor)
        .build()
}