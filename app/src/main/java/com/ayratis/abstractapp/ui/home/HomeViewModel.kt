package com.ayratis.abstractapp.ui.home

import android.content.SharedPreferences
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.ayratis.abstractapp.arch.live_data.Event
import com.ayratis.abstractapp.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val sp: SharedPreferences)
    : BaseViewModel() {

    val goToListCommand = MutableLiveData<Event<Unit>>()

    val text = ObservableField<String>("home")

    init {
        sp.edit().putInt("1", 1).apply()
    }

    fun goToList() {
        goToListCommand.value = Event(Unit)
    }
}