package com.borabor.travelguideapp.presentation.ui.trip

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.ItemTripPlanBinding
import com.borabor.travelguideapp.domain.model.Travel

class TripPlanAdapter(
    private val isTrips: Boolean,
    private val onDeleteClicked: (ImageView, ProgressBar, Travel) -> Unit,
    private val onItemClicked: (Travel) -> Unit
) : RecyclerView.Adapter<TripPlanAdapter.ViewHolder>() {

    private var list = listOf<Travel>()

    inner class ViewHolder(val view: ItemTripPlanBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.btDelete.root.setOnClickListener {
                onDeleteClicked(
                    view.btDelete.imageView,
                    view.btDelete.progressBar,
                    list[adapterPosition]
                )
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
        holder.view.travel = list[position]
        holder.view.isTrips = isTrips
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Travel>) {
        list = newList
        notifyDataSetChanged()
    }
}