package com.ayratis.abstractapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ayratis.abstractapp.arch.live_data.Event
import com.ayratis.abstractapp.arch.live_data.call
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.repository.UsersRepository
import com.ayratis.abstractapp.ui._base.BaseViewModel
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel() {

    val emptyLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val pageLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val showError: MutableLiveData<Event<Pair<String?, (() -> Unit)?>>> by lazy {
        MutableLiveData<Event<Pair<String?, (() -> Unit)?>>>()
    }

    private val pageSize = 15

    private val sourceFactory: ItemKeyedDataSourceFactory<User> by lazy {
        ItemKeyedDataSourceFactory(
            requestFactory = { key, count -> usersRepository.getUsers(key, count) },
            viewController = object :
                PagingKeyedDataSource.ViewController<User> {
                override fun showEmptyProgress(show: Boolean) {
                    emptyLoading.postValue(show)
                }

                override fun showPageProgress(show: Boolean) {
                    pageLoading.postValue(show)
                }

                override fun showError(show: Boolean, error: Throwable?, retry: (() -> Unit)?) {
                    if (show) showError.call(Pair(error?.message, retry))
                }
            })
    }

    val items = LivePagedListBuilder<Long, User>(
        sourceFactory, PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
    ).build()

    fun refresh() {
        sourceFactory.pagingKeyedDataSourceLiveData.value?.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        sourceFactory.pagingKeyedDataSourceLiveData.value?.release()
    }
}