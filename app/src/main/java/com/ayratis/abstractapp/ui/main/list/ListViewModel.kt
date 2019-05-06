package com.ayratis.abstractapp.ui.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ayratis.abstractapp.data.NetworkState
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.repository.UserRepository
import com.ayratis.abstractapp.ui._base.BaseViewModel
import javax.inject.Inject

class ListViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    private val pageSize = 15

    private val sourceFactory: UsersDataSourceFactory =
        UsersDataSourceFactory(disposables, userRepository)

    val userList = LivePagedListBuilder<Long, User>(
        sourceFactory, PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
    ).build()

    fun retry() {
        sourceFactory.usersDataSourceLiveData.value?.retry()
    }

    fun refresh() {
        sourceFactory.usersDataSourceLiveData.value?.invalidate()
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<UserDataSource, NetworkState>(sourceFactory.usersDataSourceLiveData) { it.networkState }

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<UserDataSource, NetworkState>(sourceFactory.usersDataSourceLiveData) { it.initialLoad }
}