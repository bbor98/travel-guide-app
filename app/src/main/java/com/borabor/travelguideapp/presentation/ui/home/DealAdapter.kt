package com.borabor.travelguideapp.presentation.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemDealBinding
import com.borabor.travelguideapp.domain.model.Travel

class DealAdapter(private val onItemClicked: (Travel) -> Unit) : RecyclerView.Adapter<DealAdapter.ViewHolder>() {

    private var list = listOf<Travel>()

    inner class ViewHolder(val view: ItemDealBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                onItemClicked(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_deal, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.imageUrl = list[position].images.first().url
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Travel>) {
        list = newList
        notifyDataSetChanged()
    }
}