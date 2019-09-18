package com.ayratis.abstractapp.ui.dashoard

import androidx.lifecycle.MutableLiveData
import com.ayratis.abstractapp.arch.live_data.Event
import com.ayratis.abstractapp.arch.live_data.call
import com.ayratis.abstractapp.ui._base.BaseViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor() : BaseViewModel() {

    val openCameraCommand: MutableLiveData<Event<Unit>> by lazy {
        MutableLiveData<Event<Unit>>()
    }

    val text: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "This is dashboard fragment"
        }
    }

    fun cameraClick() {
        openCameraCommand.call()
    }
}