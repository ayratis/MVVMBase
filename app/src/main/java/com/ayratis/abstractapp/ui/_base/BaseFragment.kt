package com.ayratis.abstractapp.ui._base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view) // для лисенера, рисовать на статус баре или нет
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

}
