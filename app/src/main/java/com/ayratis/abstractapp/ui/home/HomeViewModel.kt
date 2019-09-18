package com.ayratis.abstractapp.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.ayratis.abstractapp.arch.live_data.Event
import com.ayratis.abstractapp.ui._base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    val goToListCommand: MutableLiveData<Event<Unit>> by lazy {
        MutableLiveData<Event<Unit>>()
    }

    val text: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "This is home fragment"
        }
    }

    fun goToList() {
        goToListCommand.value = Event(Unit)
    }
}