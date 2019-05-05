package com.ayratis.abstractapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayratis.abstractapp.arch.live_data.Event
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {

    protected var disposables: CompositeDisposable = CompositeDisposable()
    val hideKeyboardCommand = MutableLiveData<Event<Unit>>()
    val showToastCommand = MutableLiveData<Event<String>>()
    val showSnackbarCommand = MutableLiveData<Event<String>>()

    protected fun hideKeyBoard() {
        hideKeyboardCommand.value = Event(Unit)
    }

    protected fun showToast(message: String) {
        showToastCommand.value = Event(message)
    }

    protected fun showSnackbar(message: String) {
        showSnackbarCommand.value = Event(message)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

}
