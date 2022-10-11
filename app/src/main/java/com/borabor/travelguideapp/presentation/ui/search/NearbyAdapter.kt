package com.borabor.travelguideapp.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemNearbyBinding
import com.borabor.travelguideapp.domain.model.Travel

class NearbyAdapter(
    private val onBookmarkClicked: (ImageView, ProgressBar, String, Boolean) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : ListAdapter<Travel, NearbyAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val view: ItemNearbyBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btBookmark.setOnClickListener {
                onBookmarkClicked(
                    view.ivBookmark,
                    view.pbLoading,
                    getItem(adapterPosition).id,
                    getItem(adapterPosition).isBookmark
                )
            }

            view.root.setOnClickListener {
                onItemClicked(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_nearby, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.travel = getItem(position)
    }

    object DiffCallback : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Travel, newItem: Travel) = oldItem == newItem
    }
}