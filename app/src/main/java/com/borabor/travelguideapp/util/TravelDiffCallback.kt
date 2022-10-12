package com.borabor.travelguideapp.util

import androidx.recyclerview.widget.DiffUtil
import com.borabor.travelguideapp.domain.model.Travel

object TravelDiffCallback : DiffUtil.ItemCallback<Travel>() {
    override fun areItemsTheSame(oldItem: Travel, newItem: Travel) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Travel, newItem: Travel) = oldItem == newItem
}