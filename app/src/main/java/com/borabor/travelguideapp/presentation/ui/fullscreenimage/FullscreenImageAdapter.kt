package com.borabor.travelguideapp.presentation.ui.fullscreenimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemFullscreenImageBinding
import com.borabor.travelguideapp.domain.model.Image
import com.borabor.travelguideapp.util.ImageDiffCallback

class FullscreenImageAdapter(private val onClick: () -> Unit) : ListAdapter<Image, FullscreenImageAdapter.ViewHolder>(ImageDiffCallback) {
    inner class ViewHolder(val view: ItemFullscreenImageBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.photoView.setOnClickListener { onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fullscreen_image, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.imageUrl = getItem(position).url
    }
}