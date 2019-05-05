package com.ayratis.abstractapp.ui.list

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ayratis.abstractapp.data.NetworkState
import com.ayratis.abstractapp.data.UserDataSource
import com.ayratis.abstractapp.data.UsersDataSourceFactory
import com.ayratis.abstractapp.repository.UserRepository
import com.ayratis.abstractapp.ui.base.BaseViewModel
import com.ayratis.abstractapp.entity.User
import javax.inject.Inject

class ListViewModel @Inject constructor(private val userRepository: UserRepository)
    :BaseViewModel() {
    val text = ObservableField<String>("list")

    var userList: LiveData<PagedList<User>>

    private val pageSize = 15

    private val sourceFactory: UsersDataSourceFactory = UsersDataSourceFactory(disposables, userRepository)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        userList = LivePagedListBuilder<Long, User>(sourceFactory, config).build()
    }

    fun retry() {
        sourceFactory.usersDataSourceLiveData.value?.retry()
    }

    fun refresh() {
        sourceFactory.usersDataSourceLiveData.value?.invalidate()
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<UserDataSource, NetworkState> (sourceFactory.usersDataSourceLiveData) {it.networkState}

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<UserDataSource, NetworkState> (sourceFactory.usersDataSourceLiveData) {it.initialLoad}
}