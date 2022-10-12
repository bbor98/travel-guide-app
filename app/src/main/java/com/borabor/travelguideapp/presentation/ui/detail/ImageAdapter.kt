package com.borabor.travelguideapp.presentation.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemImageBinding
import com.borabor.travelguideapp.domain.model.Image
import com.borabor.travelguideapp.util.ImageDiffCallback

class ImageAdapter(private val onItemClicked: (Int) -> Unit) : ListAdapter<Image, ImageAdapter.ViewHolder>(ImageDiffCallback) {
    inner class ViewHolder(val view: ItemImageBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_image, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.imageUrl = getItem(position).url
    }
}