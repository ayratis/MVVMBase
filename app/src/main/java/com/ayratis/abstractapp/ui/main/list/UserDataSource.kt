package com.ayratis.abstractapp.ui.main.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.ayratis.abstractapp.data.NetworkState
import com.ayratis.abstractapp.repository.UserRepository
import com.ayratis.abstractapp.entity.User
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action

class UserDataSource(
    private val userRepository: UserRepository,
    private val disposables: CompositeDisposable
) : ItemKeyedDataSource<Long, User>() {

    private val TAG = "UserDataSource"

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    private var retryCompletable: Completable? = null

    fun retry() {
        retryCompletable?.let {
            disposables.add(it
                .subscribe(
                    {},
                    {t ->
                        Log.d("retryCompletable", t.message)
                    }
                ))
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<User>) {

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        disposables.add(
            userRepository
                .getUsers(1, params.requestedLoadSize)
                .subscribe(
                    {
                        Log.d(TAG, "onNext")
                        setRetry(null)
                        networkState.postValue(NetworkState.LOADED)
                        initialLoad.postValue(NetworkState.LOADED)
                        callback.onResult(it)
                    },
                    {
                        setRetry(Action { loadInitial(params, callback) })
                        val error = NetworkState.error(it.message)
                        networkState.postValue(error)
                        initialLoad.postValue(error)
                    })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<User>) {

        networkState.postValue(NetworkState.LOADING)

        disposables.add(
            userRepository
                .getUsers(params.key, params.requestedLoadSize)
                .subscribe(
                    {
                        Log.d(TAG, "load after: onNext")
                        setRetry(null)
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(it)
                    },
                    {
                        Log.d(TAG, "load after: error" + it.message)
                        setRetry(Action { loadAfter(params, callback) })
                        networkState.postValue(NetworkState.error(it.message))
                    }
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


class UsersDataSourceFactory(private val disposables: CompositeDisposable,
                             private val userRepository: UserRepository)
    : DataSource.Factory<Long, User>() {

    val usersDataSourceLiveData = MutableLiveData<UserDataSource>()

    override fun create(): DataSource<Long, User> {
        val usersDataSource = UserDataSource(userRepository, disposables)
        usersDataSourceLiveData.postValue(usersDataSource)
        return usersDataSource
    }
}
