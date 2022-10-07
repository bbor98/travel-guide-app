package com.borabor.travelguideapp.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemNearbyBinding
import com.borabor.travelguideapp.domain.model.Travel

class NearbyAdapter(
    private val onBookmarkClicked: (String) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : ListAdapter<Travel, NearbyAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val view: ItemNearbyBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btBookmark.setOnClickListener {
                onBookmarkClicked(getItem(adapterPosition).id)
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
        val item = getItem(position)
        holder.view.apply {
            imageUrl = item.images.first().url
            country = item.country
            city = item.city
            isBookmark = item.isBookmark
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Travel, newItem: Travel) = oldItem == newItem
    }
}