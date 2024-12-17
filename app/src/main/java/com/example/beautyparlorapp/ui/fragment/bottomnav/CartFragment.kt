package com.example.beautyparlorapp.ui.fragment.bottomnav

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.ServiceListAdapter
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class CartFragment : Fragment(),ServiceListAdapter.CartUpdateListener{
    private lateinit var binding: FragmentCartBinding
    private lateinit var list:ArrayList<ServiceModel>
    private lateinit var firestore: FirebaseFirestore
    private val cartAdapter by lazy { ServiceListAdapter(onProductClick = ::onProductClick,requireContext(), list =list, isCartFragment = true, cartUpdateListener =this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore=FirebaseFirestore.getInstance()
        setUpCartRv()
        binding.btnSlotBook.setOnClickListener {
            Toast.makeText(requireContext(), "Ui is in Working Mode", Toast.LENGTH_SHORT).show()
           // findNavController().navigate(R.id.action_cartFragment_to_appoinmentFragment)
        }
    }

    private fun updateCartDetails(cartItems: ArrayList<ServiceModel>) {
        var totalPrice = 0.0
        var totalItems = 0

        for (item in cartItems) {
            // Clean the servicePrice (remove the '₹' symbol and any spaces)
            val cleanedPriceString = item.servicePrice?.replace("₹", "")?.trim()

            // Try to convert the cleaned price to a Double
            val price = cleanedPriceString?.toDoubleOrNull()

            if (price != null) {
                totalPrice += price
                totalItems++
            } else {
                // Log error if price is invalid or cannot be converted
                Log.e("CartError", "Invalid price for item: ${item.serviceName}, Price: ${item.servicePrice}")
            }

            // Debug log for each item
            Log.d("CartDebug", "Item Name: ${item.serviceName}, Price: ${item.servicePrice}, Cleaned Price: $cleanedPriceString, Parsed Price: $price")
        }

        // Calculate discount if totalPrice is greater than 5000
        val discount = if (totalPrice > 5000) totalPrice * 0.10 else 0.0
        val finalPrice = totalPrice - discount

        // Log the calculated totals
        Log.d("CartDetails", "Total Price: $totalPrice, Total Items: $totalItems, Discount: $discount")

        // Update UI with the calculated values
        binding.tvPrice.text = "₹ %.2f".format(totalPrice)
        binding.tvDiscount.text = "-₹ %.2f".format(discount)
        binding.tvTotalAmount.text = "₹ %.2f".format(finalPrice)
        binding.tvAmount.text = "₹ %.2f".format(finalPrice)
        binding.totalItems.text = "Total items ($totalItems)"
    }

    private fun setUpCartRv() {
        list=ArrayList()
        binding.orderRv.adapter=cartAdapter
        binding.orderRv.layoutManager=LinearLayoutManager(requireContext())
        binding.orderRv.setHasFixedSize(true)
        fetchCartInfo()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCartInfo() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User is not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Cart Info")
            .whereEqualTo("userId", userId) // Query using the correct field
            .get()
            .addOnSuccessListener { res ->
                list.clear()
                for (doc in res) {
                    val cart = doc.toObject(ServiceModel::class.java)
                    list.add(cart)
                }
                cartAdapter.notifyDataSetChanged()
                updateCartDetails(list)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun onProductClick(serviceModel: ServiceModel) {
    }

    override fun onCartUpdated(updatedList: ArrayList<ServiceModel>) {
        updateCartDetails(updatedList) // Update cart details
    }


}