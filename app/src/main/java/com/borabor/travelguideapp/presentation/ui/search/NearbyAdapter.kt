package com.borabor.travelguideapp.presentation.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemNearbyBinding
import com.borabor.travelguideapp.domain.model.Travel

class NearbyAdapter(
    private val onBookmarkClicked: (String) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : RecyclerView.Adapter<NearbyAdapter.ViewHolder>() {

    private var list = listOf<Travel>()

    inner class ViewHolder(val view: ItemNearbyBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btBookmark.setOnClickListener {
                onBookmarkClicked(list[adapterPosition].id)
            }

            view.root.setOnClickListener {
                onItemClicked(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_nearby, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.imageUrl = list[position].images.first().url
        holder.view.country = list[position].country
        holder.view.city = list[position].city
        // TODO
        // holder.view.isBookmark = list[position].isBookmark
        holder.view.isBookmark = false
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Travel>) {
        list = newList
        notifyDataSetChanged()
    }
}