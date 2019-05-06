package com.ayratis.abstractapp.ui.main.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.ayratis.abstractapp.data.NetworkState
import com.ayratis.abstractapp.repository.UserRepository
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.repository.UserLocalDataSource
import com.ayratis.abstractapp.repository.UserRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action

class PagingKeyedDataSource(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : ItemKeyedDataSource<Long, User>() {

    private val TAG = "UserDataSource"

    private val disposables = CompositeDisposable()

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    private var retryCompletable: Completable? = null

    fun retry() {
        retryCompletable?.let {
            disposables.add(it
                .subscribe(
                    {},
                    { t ->
                        Log.d("retryCompletable", t.message)
                    }
                ))
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<User>) {

        val remote = userRemoteDataSource
            .getUsers(1, params.requestedLoadSize)
            .doOnSubscribe {
                Log.d(TAG, "remote initial onSubscribe")
                networkState.postValue(NetworkState.LOADING)
                initialLoad.postValue(NetworkState.LOADING)
            }
            .doOnNext {
                Log.d(TAG, "remote initial onNext")
                it.forEach { user -> Log.d(TAG, "remote " + user.id.toString()) }
                setRetry(null)
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                disposables.add(userLocalDataSource.addUsers(it).subscribe())
            }
            .doOnError {
                setRetry(Action { loadInitial(params, callback) })
                val error = NetworkState.error(it.message)
                networkState.postValue(error)
                initialLoad.postValue(error)
            }

        val local = userLocalDataSource
            .getUsersSingle(1, params.requestedLoadSize)
            .toFlowable()
            .doOnNext {
                Log.d(TAG, "local loadAfter onNext")
                it.forEach { user -> Log.d(TAG, "local " + user.id.toString()) }
            }
            .flatMap {
                if (it.isNotEmpty()) {
                    Flowable.just(it)
                } else {
                    remote
                }
            }

        disposables.add(local.subscribe(
            {
                callback.onResult(it)
            },
            {}
        ))
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<User>) {

        val remote = userRemoteDataSource
            .getUsers(params.key, params.requestedLoadSize)
            .doOnSubscribe {
                Log.d(TAG, "remote loadAfter onSubscribe")
                networkState.postValue(NetworkState.LOADING)
            }
            .doOnNext {
                Log.d(TAG, "remote loadAfter onNext")
                it.forEach { user -> Log.d(TAG, "remote " + user.id.toString()) }
                setRetry(null)
                networkState.postValue(NetworkState.LOADED)
                disposables.add(userLocalDataSource.addUsers(it).subscribe())
            }
            .doOnError {
                Log.d(TAG, "load after: error" + it.message)
                setRetry(Action { loadAfter(params, callback) })
                networkState.postValue(NetworkState.error(it.message))
            }

        val local = userLocalDataSource
            .getUsersSingle(params.key, params.requestedLoadSize)
            .toFlowable()
            .doOnNext {
                Log.d(TAG, "local loadAfter onNext")
                it.forEach { user -> Log.d(TAG, "local " + user.id.toString()) }
            }

        disposables.add(local
            .flatMap {
                if (it.isNotEmpty()) {
                    Flowable.just(it)
                } else {
                    remote
                }
            }
            .subscribe(
            {
                callback.onResult(it)
            },
            {}
        ))

    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<User>) {
        //do nothing
    }

    override fun getKey(item: User): Long {
        return item.id
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

}

class PagingKeyedDataSourceFactory(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : DataSource.Factory<Long, User>() {

    val pagingKeyedDataSourceLiveData = MutableLiveData<PagingKeyedDataSource>()

    override fun create(): DataSource<Long, User> {
        val pagingKeyedDataSource = PagingKeyedDataSource(userRemoteDataSource, userLocalDataSource)
        pagingKeyedDataSourceLiveData.postValue(pagingKeyedDataSource)
        return pagingKeyedDataSource
    }
}
