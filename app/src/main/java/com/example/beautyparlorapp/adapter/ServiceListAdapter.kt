package com.example.beautyparlorapp.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.ServiceItemLayoutBinding
import com.example.beautyparlorapp.databinding.ServiceListItemLayoutBinding
import com.example.beautyparlorapp.ui.fragment.bottomnav.CartFragment
import com.google.firebase.firestore.FirebaseFirestore


class ServiceListAdapter(private val onProductClick: (ServiceModel) -> Unit, private val context: Context, private val list:ArrayList<ServiceModel>,
                         private val isCartFragment: Boolean,private val cartUpdateListener: CartUpdateListener):
    RecyclerView.Adapter<ServiceListAdapter.ViewHolder>() {



    class ViewHolder(val binding: ServiceListItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ServiceListItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model=list[position]

        holder.binding.serviceName.text=model.serviceName

        if (isCartFragment){
            holder.binding.imgCancel.visibility= View.VISIBLE
            if (position % 2 == 0) {
                holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#B1E2E2"))
            } else {
                holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#F2BBEE"))
            }
            holder.binding.serviceDuration.text="⏱${model.serviceDuration}"
            holder.binding.servicePrice.text="${model.servicePrice}"

            val firestore=FirebaseFirestore.getInstance()
            holder.binding.imgCancel.setOnClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Delete Cart Item")
                    .setMessage("Are you sure you want to delete this item from your cart?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        // Delete the cart item
                        val itemId = model.documentId // Ensure `id` is set in your model

                        if (itemId != null) {
                            Log.d("SerViceListAdapter",itemId)
                            firestore.collection("Cart Info").document(itemId)
                                .delete()
                                .addOnSuccessListener {
                                    list.removeAt(position)
                                    notifyItemRemoved(position)
                                    cartUpdateListener.onCartUpdated(list)
                                    Toast.makeText(holder.itemView.context, "Item deleted", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(holder.itemView.context, "Error deleting item: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }else{
            holder.binding.imgCancel.visibility= View.GONE
            if (position % 2 == 0) {
                holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#F2BBEE"))
            } else {
                holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#B1E2E2"))
            }
            holder.binding.serviceDuration.text="Service Duration:${model.serviceDuration}hrs"
            holder.binding.servicePrice.text="Service Price:${model.servicePrice}Rs"
        }
        holder.binding.root.setOnClickListener { onProductClick(model) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CartUpdateListener {
        fun onCartUpdated(updatedList: ArrayList<ServiceModel>)
    }
}