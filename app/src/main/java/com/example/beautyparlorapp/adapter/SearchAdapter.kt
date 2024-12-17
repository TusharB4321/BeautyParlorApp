package com.example.beautyparlorapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.ServiceItemLayoutBinding
import com.example.beautyparlorapp.databinding.ServiceListItemLayoutBinding
import com.example.beautyparlorapp.utils.FilterServices

class SearchAdapter(private val onProductClick: (ServiceModel) -> Unit, private val context: Context, private val list: ArrayList<ServiceModel>)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>(),Filterable {


    class ViewHolder(val binding: ServiceListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil=object:DiffUtil.ItemCallback<ServiceModel>(){
        override fun areItemsTheSame(oldItem: ServiceModel, newItem: ServiceModel): Boolean {
            return oldItem.userId==newItem.userId
        }

        override fun areContentsTheSame(oldItem: ServiceModel, newItem: ServiceModel): Boolean {
           return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ServiceListItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = differ.currentList[position]

        holder.binding.serviceName.text=model.serviceName
        holder.binding.servicePrice.text="Service Price:${model.servicePrice}Rs"
        holder.binding.serviceDuration.text="Service Duration:${model.serviceDuration}hrs"

        holder.binding.root.setOnClickListener {
            onProductClick(model)  // Pass category name on click
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val filter:FilterServices?=null
    var originalList=ArrayList<ServiceModel>()
    override fun getFilter(): Filter {
        if (filter==null)return FilterServices(this,originalList)
        return filter
    }
}

