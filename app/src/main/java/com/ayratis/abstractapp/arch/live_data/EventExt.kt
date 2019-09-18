package com.ayratis.abstractapp.arch.live_data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner, Observer { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) })
}

fun MutableLiveData<Event<Unit>>.call() {
    this.value = Event(Unit)
}

fun <T> MutableLiveData<Event<T>>.call(t: T) {
    this.value = Event(t)
}