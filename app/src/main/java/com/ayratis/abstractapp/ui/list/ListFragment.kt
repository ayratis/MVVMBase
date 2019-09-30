package com.ayratis.abstractapp.ui.list

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.live_data.observeEvent
import com.ayratis.abstractapp.arch.view_model.viewModelProvider
import com.ayratis.abstractapp.databinding.FragmentListBinding
import com.ayratis.abstractapp.ui._base.BaseBindingFragment
import com.ayratis.abstractapp.ui.adapters.UsersAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : BaseBindingFragment<FragmentListBinding>() {

    override val layoutId = R.layout.fragment_list

    private val viewModel: ListViewModel by viewModelProvider()

    private val userAdapter: UsersAdapter by lazy {
        UsersAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
        binding.usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
        viewModel.items.observe(this, Observer { userAdapter.submitList(it) })
        viewModel.pageLoading.observe(this, Observer { userAdapter.setPageLoading(it) })
        viewModel.showError.observeEvent(this) { (message, action) ->
            view?.run {
                Snackbar.make(this, message ?: "", Snackbar.LENGTH_INDEFINITE).apply {
                    setAction(R.string.retry) {
                        action?.invoke()
                        dismiss()
                    }
                }.show()

            }
        }
        viewModel.emptyLoading.observe(this, Observer {
            usersSwipeRefreshLayout.post { usersSwipeRefreshLayout.isRefreshing = it }
        })
        usersSwipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

}
