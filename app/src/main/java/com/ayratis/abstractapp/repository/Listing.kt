package com.ayratis.abstractapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ayratis.abstractapp.data.NetworkState

data class Listing<T> (
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)