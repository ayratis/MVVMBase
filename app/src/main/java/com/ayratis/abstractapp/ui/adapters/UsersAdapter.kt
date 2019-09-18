package com.ayratis.abstractapp.ui.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.databinding.ItemPageLoadingBinding
import com.ayratis.abstractapp.databinding.ItemUserBinding
import com.ayratis.abstractapp.entity.User
import com.ayratis.abstractapp.ui.views.BindingViewHolder
import java.util.concurrent.atomic.AtomicBoolean

class UsersAdapter : PagedListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback) {

    private val pageLoading: AtomicBoolean by lazy {
        AtomicBoolean(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> BindingViewHolder.create<ItemUserBinding>(parent, viewType)
            R.layout.item_page_loading -> BindingViewHolder.create<ItemPageLoadingBinding>(parent, viewType)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as BindingViewHolder<ItemUserBinding>).binding.user = getItem(position)
        }
    }

    private fun hasExtraRow(): Boolean {
        return pageLoading.get()
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_page_loading
        } else {
            R.layout.item_user
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setPageLoading(loading: Boolean) {
        currentList?.let {
            if (it.isNotEmpty()) {
                val previousState = pageLoading.get()
                val hadExtraRow = hasExtraRow()
                pageLoading.set(loading)
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState != loading) {
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

