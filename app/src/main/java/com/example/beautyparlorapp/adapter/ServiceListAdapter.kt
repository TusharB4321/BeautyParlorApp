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


class ServiceListAdapter(
    private val onProductClick: (ServiceModel) -> Unit,
    private val context: Context,
    private val list: ArrayList<ServiceModel>,
    private val isCartFragment: Boolean,
    private val cartUpdateListener: CartUpdateListener
) : RecyclerView.Adapter<ServiceListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ServiceListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ServiceListItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        setUpItemView(holder, model, position)
        setClickListeners(holder, model, position)
    }

    override fun getItemCount(): Int = list.size

    private fun setUpItemView(holder: ViewHolder, model: ServiceModel, position: Int) {
        holder.binding.serviceName.text = model.serviceName

        if (isCartFragment) {
            setCartFragmentView(holder, model, position)
        } else {
            setServiceFragmentView(holder, model, position)
        }
    }

    private fun setCartFragmentView(holder: ViewHolder, model: ServiceModel, position: Int) {
        holder.binding.imgCancel.visibility = View.VISIBLE
        holder.binding.serviceDuration.text = "⏱${model.serviceDuration}"
        holder.binding.servicePrice.text = "${model.servicePrice}"

        setRowBackgroundColor(holder, position)

        holder.binding.imgCancel.setOnClickListener {
            showDeleteConfirmationDialog(holder, model, position)
        }
    }

    private fun setServiceFragmentView(holder: ViewHolder, model: ServiceModel, position: Int) {
        holder.binding.imgCancel.visibility = View.GONE
        holder.binding.serviceDuration.text = "Service Duration: ${model.serviceDuration}hrs"
        holder.binding.servicePrice.text = "Service Price: ${model.servicePrice}Rs"

        setRowBackgroundColor(holder, position)
    }

    private fun setRowBackgroundColor(holder: ViewHolder, position: Int) {
        val backgroundColor = if (position % 2 == 0) "#B1E2E2" else "#F2BBEE"
        holder.binding.mainLayout.setBackgroundColor(Color.parseColor(backgroundColor))
    }

    private fun setClickListeners(holder: ViewHolder, model: ServiceModel, position: Int) {
        holder.binding.root.setOnClickListener { onProductClick(model) }
    }

    private fun showDeleteConfirmationDialog(holder: ViewHolder, model: ServiceModel, position: Int) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete Cart Item")
            .setMessage("Are you sure you want to delete this item from your cart?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteCartItem(holder, model, position)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deleteCartItem(holder: ViewHolder, model: ServiceModel, position: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val itemId = model.documentId ?: return

        firestore.collection("Cart Info").document(itemId)
            .delete()
            .addOnSuccessListener {
                list.removeAt(position)
                notifyItemRemoved(position)
                cartUpdateListener.onCartUpdated(list)
                if (list.isEmpty()) {
                    cartUpdateListener.onCartEmpty()
                }
                Toast.makeText(holder.itemView.context, "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(holder.itemView.context, "Error deleting item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    interface CartUpdateListener {
        fun onCartUpdated(updatedList: ArrayList<ServiceModel>)
        fun onCartEmpty()
    }
}
