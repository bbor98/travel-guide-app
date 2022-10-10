package com.borabor.travelguideapp.presentation.ui.trip

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemTripPlanBinding
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.util.calculateDaysBetweenDates

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
        val item = list[position]

        holder.view.apply {
            imageUrl = item.images.first().url
            title = item.city
            text = if (this@TripPlanAdapter.isTrips) item.schedule else item.country
            isTrips = this@TripPlanAdapter.isTrips
            if (this@TripPlanAdapter.isTrips && list.isNotEmpty()) {
                imageCount = item.images.size
                tripLength = item.schedule?.calculateDaysBetweenDates()
            }
        }
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Travel>) {
        list = newList
        notifyDataSetChanged()
    }
}