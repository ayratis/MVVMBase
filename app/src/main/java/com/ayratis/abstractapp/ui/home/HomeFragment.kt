package com.ayratis.abstractapp.ui.home


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ayratis.abstractapp.App
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.ui.base.BaseMVVMFragment

class HomeFragment : BaseMVVMFragment<com.ayratis.abstractapp.databinding.FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel
        get() = injectViewModel(viewModelFactory)
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun daggerInject() {
        App.getAppComponent().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupGoToList()
    }

    private fun setupGoToList() {
        viewModel.goToListCommand.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(R.id.action_homeFragment_to_listFragment)
            }
        })
    }


}
