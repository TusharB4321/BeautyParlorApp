package com.example.beautyparlorapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.ServiceItemLayoutBinding
import com.example.beautyparlorapp.databinding.ServiceListItemLayoutBinding

class ServiceListAdapter(private val onProductClick: (ServiceModel) -> Unit, private val context: Context, private val list:ArrayList<ServiceModel>):
    RecyclerView.Adapter<ServiceListAdapter.ViewHolder>() {

    class ViewHolder( val binding: ServiceListItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ServiceListItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model=list[position]

        holder.binding.serviceName.text=model.serviceName
        holder.binding.serviceDuration.text="Service Duration: ${model.serviceDuration}h"
        holder.binding.servicePrice.text="Service Price: ${model.servicePrice}Rs"

        holder.binding.root.setOnClickListener { onProductClick(model) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}