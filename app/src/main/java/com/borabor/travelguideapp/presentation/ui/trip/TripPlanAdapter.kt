package com.borabor.travelguideapp.presentation.ui.trip

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemTripPlanBinding
import com.borabor.travelguideapp.domain.model.Travel

class TripPlanAdapter(
    private val isTrips: Boolean,
    private val onIconClicked: (Travel) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : RecyclerView.Adapter<TripPlanAdapter.ViewHolder>() {

    private var list = listOf<Travel>()

    inner class ViewHolder(val view: ItemTripPlanBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btDelete.setOnClickListener {
                onIconClicked(list[adapterPosition])
            }

            view.root.setOnClickListener {
                onItemClicked(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_trip_plan, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.imageUrl = list[position].images.first().url
        holder.view.title = list[position].city
        holder.view.text = list[position].country
        holder.view.isTrips = isTrips
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Travel>) {
        list = newList
        notifyDataSetChanged()
    }
}