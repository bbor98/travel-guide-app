package com.borabor.travelguideapp.presentation.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemTopPickBinding
import com.borabor.travelguideapp.domain.model.Travel

class TopPickAdapter(
    private val onBookmarkClicked: (ImageView, ProgressBar, String) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : ListAdapter<Travel, TopPickAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val view: ItemTopPickBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btBookmark.setOnClickListener {
                onBookmarkClicked(
                    view.ivBookmark,
                    view.pbLoading,
                    getItem(adapterPosition).id
                )
            }

            view.root.setOnClickListener {
                onItemClicked(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_top_pick, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.view.apply {
            imageUrl = item.images.first().url
            title = item.title
            description = item.description
            isBookmark = item.isBookmark
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Travel, newItem: Travel) = oldItem == newItem
    }
}