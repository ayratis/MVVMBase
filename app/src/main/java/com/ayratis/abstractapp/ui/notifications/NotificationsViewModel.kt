package com.ayratis.abstractapp.ui.notifications

import android.content.SharedPreferences
import androidx.databinding.ObservableField
import com.ayratis.abstractapp.ui.base.BaseViewModel
import javax.inject.Inject

class NotificationsViewModel
@Inject constructor(private val sp: SharedPreferences)
    :BaseViewModel(){

    val text = ObservableField<String>("notifications")

    init {
        sp.edit().putInt("1", 1).apply()
    }
}