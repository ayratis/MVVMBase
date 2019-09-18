package com.ayratis.abstractapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class PagingKeyedDataSource<T : PagingKeyedDataSource.ItemWithStableId>(
    private val requestFactory: (Long, Int) -> Single<List<T>>,
    private val viewController: ViewController<T>
) : ItemKeyedDataSource<Long, T>() {

    interface ViewController<T> {
        fun showEmptyProgress(show: Boolean)
        fun showPageProgress(show: Boolean)
        fun showError(show: Boolean, error: Throwable? = null, retry: (() -> Unit)? = null)
    }

    interface ItemWithStableId {
        fun getStableId(): Long
    }

    private var disposable: Disposable? = null

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<T>) {

        disposable?.dispose()
        disposable = requestFactory.invoke(1, params.requestedLoadSize)
            .doOnSubscribe {
                viewController.showEmptyProgress(true)
                viewController.showError(false)
                viewController.showPageProgress(false)
            }
            .subscribe(
                {
                    viewController.showEmptyProgress(false)
                    viewController.showError(false)
                    viewController.showPageProgress(false)
                    callback.onResult(it)
                },
                {
                    viewController.showEmptyProgress(false)
                    viewController.showPageProgress(false)
                    viewController.showError(true, it) { loadInitial(params, callback) }
                }
            )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<T>) {

        disposable?.dispose()
        disposable = requestFactory.invoke(params.key, params.requestedLoadSize)
            .doOnSubscribe {
                viewController.showEmptyProgress(false)
                viewController.showError(false)
                viewController.showPageProgress(true)
            }
            .subscribe(
                {
                    viewController.showEmptyProgress(false)
                    viewController.showError(false)
                    viewController.showPageProgress(false)
                    callback.onResult(it)
                },
                {
                    viewController.showEmptyProgress(false)
                    viewController.showPageProgress(false)
                    viewController.showError(true, it) { loadAfter(params, callback) }
                }
            )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<T>) {
        //do nothing
    }

    override fun getKey(item: T): Long {
        return item.getStableId()
    }

    fun release() {
        disposable?.dispose()
    }
}

class ItemKeyedDataSourceFactory<T : PagingKeyedDataSource.ItemWithStableId>(
    private val requestFactory: (Long, Int) -> Single<List<T>>,
    private val viewController: PagingKeyedDataSource.ViewController<T>
) : DataSource.Factory<Long, T>() {

    val pagingKeyedDataSourceLiveData: MutableLiveData<PagingKeyedDataSource<T>> by lazy {
        MutableLiveData<PagingKeyedDataSource<T>>()
    }

    override fun create(): DataSource<Long, T> {
        val pagingKeyedDataSource =
            PagingKeyedDataSource(requestFactory, viewController)
        pagingKeyedDataSourceLiveData.postValue(pagingKeyedDataSource)
        return pagingKeyedDataSource
    }
}
