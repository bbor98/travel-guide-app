package com.borabor.travelguideapp.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemNearbyBinding
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.util.TravelDiffCallback

class NearbyAdapter(
    private val onBookmarkClicked: (ImageView, ProgressBar, String, Boolean) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : ListAdapter<Travel, NearbyAdapter.ViewHolder>(TravelDiffCallback) {
    inner class ViewHolder(val view: ItemNearbyBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btBookmark.root.setOnClickListener {
                onBookmarkClicked(
                    view.btBookmark.imageView,
                    view.btBookmark.progressBar,
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
}