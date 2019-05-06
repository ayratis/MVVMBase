package com.ayratis.abstractapp.ui.main.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.data.NetworkState
import com.ayratis.abstractapp.data.Status
import com.ayratis.abstractapp.di.injector
import com.ayratis.abstractapp.ui._base.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_network_state.*

class ListFragment : BaseMVVMFragment<com.ayratis.abstractapp.databinding.FragmentListBinding, ListViewModel>() {

    private lateinit var userAdapter: UsersAdapter

    override val viewModel: ListViewModel
        get() = injectViewModel(viewModelFactory)
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun daggerInject() {
        injector().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        initSwipeToRefresh()
    }

    private fun setupAdapter() {
        userAdapter = UsersAdapter { viewModel.retry() }
        binding.usersList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.usersList.adapter = userAdapter
        viewModel.userList.observe(this, Observer {
            userAdapter.submitList(it)
        })
        viewModel.getNetworkState().observe(this, Observer {
            userAdapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        viewModel.getRefreshState().observe(this, Observer { networkState ->
            if (userAdapter.currentList != null) {
                if (userAdapter.currentList!!.size > 0) {
                    usersSwipeRefreshLayout.isRefreshing = networkState?.status == NetworkState.LOADING.status
                } else {
                    setInitialLoadingState(networkState)
                }
            } else {
                setInitialLoadingState(networkState)
            }
        })
        usersSwipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun setInitialLoadingState(networkState: NetworkState?) {
        //error message
        errorMessageTextView.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
        if (networkState?.message != null) {
            errorMessageTextView.text = networkState.message
        }

        //loading and retry
        retryLoadingButton.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        loadingProgressBar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE

        usersSwipeRefreshLayout.isEnabled = networkState?.status == Status.SUCCESS
        retryLoadingButton.setOnClickListener { viewModel.retry() }
    }


}
