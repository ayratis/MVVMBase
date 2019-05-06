package com.ayratis.abstractapp.ui.main.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.Action
import com.ayratis.abstractapp.data.NetworkState
import com.ayratis.abstractapp.databinding.ItemNetworkStateBinding
import com.ayratis.abstractapp.databinding.ItemUserBinding
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.views.BindingViewHolder

class UsersAdapter(private val retryCallback: () -> Unit) :
    PagedListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> BindingViewHolder.create<ItemUserBinding>(parent, viewType)
            R.layout.item_network_state -> BindingViewHolder.create<ItemNetworkStateBinding>(parent, viewType)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as BindingViewHolder<ItemUserBinding>).binding.user = getItem(position)
            R.layout.item_network_state -> {
                val binding = (holder as BindingViewHolder<ItemNetworkStateBinding>).binding
                binding.networkState = networkState
                binding.retry = Action { retryCallback() }
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_user
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        currentList?.let {
            if (it.isNotEmpty()) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        val UserDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

}

