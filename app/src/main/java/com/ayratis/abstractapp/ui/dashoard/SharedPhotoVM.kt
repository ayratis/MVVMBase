package com.ayratis.abstractapp.ui.dashoard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayratis.abstractapp.arch.live_data.Event

class SharedPhotoVM : ViewModel() {
    val imageCroppedCommand: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }
}