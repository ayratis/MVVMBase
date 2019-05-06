package com.ayratis.abstractapp.ui.main.dashoard

import android.content.SharedPreferences
import androidx.databinding.ObservableField
import com.ayratis.abstractapp.ui._base.BaseViewModel
import javax.inject.Inject

class DashboardViewModel
    @Inject constructor(private val sp: SharedPreferences)
    : BaseViewModel() {

    val text = ObservableField<String>("dashboard")

    init {
        sp.edit().putInt("1", 1).apply()
    }
}