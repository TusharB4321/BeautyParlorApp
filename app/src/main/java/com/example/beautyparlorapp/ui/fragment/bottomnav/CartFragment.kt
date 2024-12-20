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
import com.example.beautyparlorapp.utils.Constant
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class CartFragment : Fragment(), ServiceListAdapter.CartUpdateListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var list: ArrayList<ServiceModel>
    private lateinit var firestore: FirebaseFirestore
    private var totalPrice = 0.0
    private var totalItems = 0
    private var discount = 0.0
    private val cartAdapter by lazy {
        ServiceListAdapter(
            onProductClick = ::onProductClick,
            requireContext(),
            list = list,
            isCartFragment = true,
            cartUpdateListener = this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        setUpCartRv()
        setUpListener()
    }

    private fun setUpListener() {
        binding.btnSlotBook.setOnClickListener {
            val bundle = Bundle().apply {
                putDouble("price", totalPrice)
                putDouble("discount", discount)
                putInt("totalItems", totalItems)
            }
            findNavController().navigate(
                R.id.action_cartFragment_to_appointmentFragment,
                bundle,
                Constant.slideRightLeftNavOptions
            )
        }

        binding.btnBookNow.setOnClickListener {
            // Navigate to HomeFragment
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)

            // Update the BottomNavigationView to select the Home icon
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomBar) // Replace with your BottomNavigationView ID
            bottomNav.selectedItemId = R.id.home // Replace with the item ID for the home fragment in the BottomNavigationView
        }

    }

    private fun updateCartDetails(cartItems: ArrayList<ServiceModel>) {
        totalPrice = 0.0
        totalItems = 0

        for (item in cartItems) {
            val cleanedPriceString = item.servicePrice?.replace("₹", "")?.trim()
            val price = cleanedPriceString?.toDoubleOrNull()

            if (price != null) {
                totalPrice += price
                totalItems++
            } else {
                Log.e("CartError", "Invalid price for item: ${item.serviceName}, Price: ${item.servicePrice}")
            }
        }

        discount = if (totalPrice > 5000) totalPrice * 0.10 else 0.0
        val finalPrice = totalPrice - discount

        binding.tvPrice.text = "₹ %.2f".format(totalPrice)
        binding.tvDiscount.text = "-₹ %.2f".format(discount)
        binding.tvTotalAmount.text = "₹ %.2f".format(finalPrice)
        binding.tvAmount.text = "₹ %.2f".format(finalPrice)
        binding.totalItems.text = "Total items ($totalItems)"
    }

    private fun setUpCartRv() {
        list = ArrayList()
        binding.orderRv.adapter = cartAdapter
        binding.orderRv.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRv.setHasFixedSize(true)
        fetchCartInfo()
    }

    private fun fetchCartInfo() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User is not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Cart Info")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { res ->
                list.clear()
                for (doc in res) {
                    val cart = doc.toObject(ServiceModel::class.java)
                    list.add(cart)
                }

                if (list.isEmpty()) {
                    showEmptyCart()
                } else {
                    showCartDetails()
                    cartAdapter.notifyDataSetChanged()
                    updateCartDetails(list)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onProductClick(serviceModel: ServiceModel) {
        // Handle product click logic if required
    }

    private fun showEmptyCart() {
        binding.emptyCartLayout.visibility = View.VISIBLE
        binding.nestedView.visibility = View.GONE
        binding.linearLayout.visibility = View.GONE
    }

    private fun showCartDetails() {
        binding.emptyCartLayout.visibility = View.GONE
        binding.nestedView.visibility = View.VISIBLE
        binding.linearLayout.visibility = View.VISIBLE
    }

    override fun onCartUpdated(updatedList: ArrayList<ServiceModel>) {
        if (updatedList.isEmpty()) {
            showEmptyCart()
        } else {
            updateCartDetails(updatedList)
        }
    }

    override fun onCartEmpty() {
       showEmptyCart()
    }
}
