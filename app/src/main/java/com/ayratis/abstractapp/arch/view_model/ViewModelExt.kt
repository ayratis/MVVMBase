package com.ayratis.abstractapp.arch.view_model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ayratis.abstractapp.App
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.obtainSharedVM(activity: FragmentActivity?): T {
    return activity?.run {
        ViewModelProviders.of(this).get(T::class.java)
    } ?: throw Exception("Invalid Activity")
}


inline fun <reified T : ViewModel> viewModelProvider(): ReadWriteProperty<Fragment, T> =
    ViewModelDelegate { thisRef ->
        ViewModelProviders.of(thisRef, App.appComponent.viewModelFactory())[T::class.java]
    }

class ViewModelDelegate<R, T>(private val initializer: (R) -> T) : ReadWriteProperty<R, T> {

    private object EMPTY

    private var value: Any? = EMPTY

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        this.value = value
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        if (value == EMPTY) {
            value = initializer(thisRef)
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}