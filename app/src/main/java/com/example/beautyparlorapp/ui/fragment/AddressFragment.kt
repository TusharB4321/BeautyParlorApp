package com.example.beautyparlorapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.beautyparlorapp.databinding.BottomAddressDialogBinding
import com.example.beautyparlorapp.databinding.FragmentAddressBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Context

class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var currentAddress: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase instances
        database = FirebaseDatabase.getInstance().getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()

        // Load the saved address
        loadAddress()

        // Set the address to EditText if available
        binding.etAddress.setText(currentAddress)

        binding.etAddress.setOnClickListener {
            if (currentAddress.isNullOrEmpty()) {
                showBottomDialog(isUpdate = false)
            }
        }

        binding.clickHere.setOnClickListener {
            if (!currentAddress.isNullOrEmpty()) {
                showBottomDialog(isUpdate = true)
            }
        }
    }

    private fun loadAddress() {
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        currentAddress = sharedPref.getString("address", null)
    }

    private fun showBottomDialog(isUpdate: Boolean) {
        val dialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = BottomAddressDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        if (isUpdate) {
            bottomSheetBinding.btnAddAddress.text = "Update Address"
        }

        bottomSheetBinding.btnAddAddress.setOnClickListener {
            val flatNumber = bottomSheetBinding.etFlatNumber.text.toString().trim()
            val street = bottomSheetBinding.etStreet.text.toString().trim()
            val city = bottomSheetBinding.etCity.text.toString().trim()
            val pinCode = bottomSheetBinding.etPinCode.text.toString().trim()

            if (flatNumber.isEmpty() || street.isEmpty() || city.isEmpty() || pinCode.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val address = "$flatNumber, $street, $city - $pinCode"
                saveAddressToFirebase(address, isUpdate)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun saveAddressToFirebase(address: String, isUpdate: Boolean) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val addressRef = database.child(userId).child("address")

            addressRef.setValue(address).addOnSuccessListener {
                currentAddress = address
                binding.etAddress.setText(address) // Update the EditText with new address

                // Save to SharedPreferences to persist the address
                val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("address", address)
                    apply()
                }

                Toast.makeText(requireContext(), if (isUpdate) "Address updated successfully" else "Address added successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save address", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
