package com.borabor.travelguideapp.presentation.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemMightNeedBinding
import com.borabor.travelguideapp.domain.model.Travel

class MightNeedAdapter(private val onItemClicked: (Travel) -> Unit) : ListAdapter<Travel, MightNeedAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val view: ItemMightNeedBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                onItemClicked(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_might_need, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.travel = getItem(position)
    }

    object DiffCallback : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Travel, newItem: Travel) = oldItem == newItem
    }
}