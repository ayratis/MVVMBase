package com.ayratis.abstractapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ayratis.abstractapp.BR
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

abstract class BaseMVVMFragment<B : ViewDataBinding, VM : BaseViewModel> : BaseFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var binding: B
    protected abstract val viewModel: VM

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daggerInject()
    }

    abstract fun daggerInject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setVariable(BR.vm, viewModel)
        binding.executePendingBindings()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupHideKeyboardObserver()
        setupShowSnackbarObserver()
        setupToastToastObserver()
    }

    private fun setupHideKeyboardObserver() {
        viewModel.hideKeyboardCommand.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                hideKeyboard()
            }
        })
    }

    private fun setupShowSnackbarObserver() {
        viewModel.showSnackbarCommand.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                view?.run {
                    Snackbar.make(this, it, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    protected fun setupToastToastObserver() {
        viewModel.showToastCommand.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                view?.run {
                    Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
