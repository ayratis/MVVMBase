package com.ayratis.abstractapp.ui._base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseFragment: Fragment() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view) // для лисенера, рисовать на статус баре или нет
        firebaseAnalytics = FirebaseAnalytics.getInstance(view.context)
    }

    protected fun hideKeyboard() {
        activity?.run {
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = this.currentFocus
            if (view == null) {
                view = View(activity)
            }

            view.clearFocus()
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            firebaseAnalytics.setCurrentScreen(it, this.javaClass.simpleName, this.javaClass.simpleName)
        }
    }

}
