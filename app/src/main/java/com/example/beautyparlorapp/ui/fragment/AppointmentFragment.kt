package com.example.beautyparlorapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentAppointmentBinding
import com.example.beautyparlorapp.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentBinding
    private var selectedTime: TextView? = null
    private var selectedBookingDate: Date? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var currentAddress: String? = ""
    private var  totalPrice = 0.0
    private var totalItems = 0
    private var discount = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPriceDetails()
        clickListenerUI()
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        fetchAddress()
    }

    private fun fetchPriceDetails() {
        totalPrice=requireArguments().getDouble("price")
        totalItems=requireArguments().getInt("totalItems")
        discount=requireArguments().getDouble("discount")
    }

    private fun fetchAddress() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            databaseReference.child(userId)
                .child("address")
                .get()
                .addOnSuccessListener { res ->
                    currentAddress = res.getValue(String::class.java)
                    if (currentAddress != null) {
                        binding.etAddress.setText(currentAddress)
                        binding.tvaddAddress.text = "Update Address"
                    } else {
                        Toast.makeText(requireContext(), "Address not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error fetching address", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun clickListenerUI() {
        binding.calenderDatePicker.getSelectedDate { selectedDate ->
            if (selectedDate != null) {
                selectedBookingDate = selectedDate
                binding.calenderDatePicker.setBackgroundResource(R.drawable.time_selected_background)
                val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)
                Toast.makeText(requireContext(), "Selected Date: $formattedDate", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvaddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentFragment_to_addressFragment, null, Constant.slideRightLeftNavOptions)
        }

        binding.btnCheckout.setOnClickListener {

            val formattedDate = selectedBookingDate?.let {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
            } ?: ""

            val formattedTime = selectedTime?.text?.toString() ?: ""

            if (currentAddress.isNullOrEmpty()||formattedDate.isEmpty()||formattedTime.isEmpty()){
                Toast.makeText(requireContext(), "Please select required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val bundle = Bundle().apply {
                putString("bookingDate", formattedDate)
                putString("bookingTime", formattedTime)
                putString("address", currentAddress)
                putDouble("totalPrice",totalPrice)
                putDouble("discount",discount)
                putInt("totalItems",totalItems)
            }

            findNavController().navigate(R.id.action_appointmentFragment_to_paymentFragment, bundle, Constant.slideRightLeftNavOptions)
        }

        gridLayoutLogic()
    }

    private fun gridLayoutLogic() {
        for (i in 0 until binding.gridLayout.childCount) {
            val child = binding.gridLayout.getChildAt(i)
            if (child is TextView) {
                child.setOnClickListener {
                    binding.calenderDatePicker.setBackgroundColor(Color.WHITE)
                    handleTimeSelection(child)
                }
            }
        }
    }

    private fun handleTimeSelection(selectedView: TextView) {
        // Reset the background of the previously selected TextView
        selectedTime?.setBackgroundResource(R.drawable.time_background)

        // Change the background of the newly selected TextView
        selectedView.setBackgroundResource(R.drawable.time_selected_background)

        // Show a toast message with the selected time
        val timeText = selectedView.text.toString()
        Toast.makeText(requireContext(), "Selected Time: $timeText", Toast.LENGTH_SHORT).show()

        // Update the selected TextView reference
        selectedTime = selectedView
    }
}
