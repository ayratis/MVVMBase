package com.ayratis.abstractapp.ui.home


import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.ayratis.abstractapp.App
import com.ayratis.abstractapp.BR
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.live_data.observeEvent
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.arch.view_model.viewModelProvider
import com.ayratis.abstractapp.databinding.FragmentHomeBinding
import com.ayratis.abstractapp.ui._base.BaseBindingFragment

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    override val layoutId = R.layout.fragment_home
    private val viewModel: HomeViewModel by viewModelProvider()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        setupGoToList()
    }

    private fun setupGoToList() {
        viewModel.goToListCommand.observeEvent(this) {
            findNavController().navigate(R.id.action_homeFragment_to_listFragment)
        }
    }


}
