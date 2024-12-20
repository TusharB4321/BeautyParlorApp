package com.example.beautyparlorapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyparlorapp.data.UpcomingServiceModel
import com.example.beautyparlorapp.databinding.ItemUpcomingBinding

class UpcomingAdapter(private val services: List<UpcomingServiceModel>) : RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        val binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val service = services[position]
        holder.bind(service)
    }

    override fun getItemCount(): Int = services.size

    inner class UpcomingViewHolder(private val binding: ItemUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(service: UpcomingServiceModel) {
            binding.serviceName.text = service.serviceName
            binding.bookingDate.text = "Booking Date: "+service.bookingDate
            binding.bookingTime.text = "Booking Time: "+service.bookingTime
            binding.status.text = "status: "+service.status  // Display status (Pending by default)
        }
    }
}
