package com.ayratis.abstractapp.ui.notifications

import androidx.lifecycle.MutableLiveData
import com.ayratis.abstractapp.ui._base.BaseViewModel
import javax.inject.Inject

class NotificationsViewModel @Inject constructor() :BaseViewModel(){

    val text: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "This is notifications fragment"
        }
    }

}