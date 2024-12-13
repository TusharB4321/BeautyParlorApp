package com.example.beautyparlorapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.ServiceItemLayoutBinding

class ServiceAdapter(private val onProductClick: (ServiceModel) -> Unit, private val context: Context, private val list: ArrayList<ServiceModel>)
    : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    private val categoryImgMap = mapOf(
        "Makeup" to R.drawable.makeup_cat,
        "Facial" to R.drawable.facial_cat,
        "Waxing" to R.drawable.waxing_cat,
        "Threading" to R.drawable.threading_ser,
        "Hair color" to R.drawable.hair_cat,
        "Mahendi" to R.drawable.mehendi_service
    )
    class ViewHolder(val binding: ServiceItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ServiceItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        // Set the category name in the TextView
        holder.binding.category.text =model.serviceCategory

        // Assuming a default image for categories
        val imgRes = categoryImgMap[model.serviceCategory]?:R.drawable.facial_cat  // Set a default image if needed
        holder.binding.img.setImageResource(imgRes)

        // Handle click on category
        holder.binding.root.setOnClickListener {
            onProductClick(model)  // Pass category name on click
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

