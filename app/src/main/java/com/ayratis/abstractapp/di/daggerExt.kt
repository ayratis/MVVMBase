package com.ayratis.abstractapp.di

import androidx.fragment.app.Fragment
import com.ayratis.abstractapp.App

fun Fragment.injector(): AppComponent {
    return App.getAppComponent()
}