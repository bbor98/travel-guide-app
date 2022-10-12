package com.borabor.travelguideapp.util

import androidx.recyclerview.widget.DiffUtil
import com.borabor.travelguideapp.domain.model.Image

object ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image) = oldItem.url == newItem.url
    override fun areContentsTheSame(oldItem: Image, newItem: Image) = oldItem == newItem
}