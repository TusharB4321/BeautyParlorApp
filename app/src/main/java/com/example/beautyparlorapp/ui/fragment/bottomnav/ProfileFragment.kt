package com.example.beautyparlorapp.ui.fragment.bottomnav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentProfileBinding
import com.example.beautyparlorapp.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        showProfileFields()
    }

    private fun initListeners() {
        binding.btnEditProfile.setOnClickListener {
            navigateToEditProfile()
        }
        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun navigateToEditProfile() {
        findNavController().navigate(
            R.id.action_profileFragment_to_editProfileFragment,
            null,
            Constant.slideRightLeftNavOptions
        )
    }

    private fun showProfileFields() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

            // Fetch data from the database
            userRef.get()
                .addOnSuccessListener { res ->
                    if (res.exists()) { // Check if data exists
                        val fullName = res.child("fullName").value?.toString() ?: ""
                        val email = res.child("email").value?.toString() ?: ""
                        val contactNumber = res.child("contactNumber").value?.toString() ?: ""

                        Log.d("ABC", "FullName: $fullName, Email: $email, Contact: $contactNumber")

                        // Prefill fields with fetched data
                        binding.etProfileName.setText(fullName)
                        binding.etProfileEmail.setText(email)
                        binding.etProfileNumber.setText(contactNumber)
                    } else {
                        Log.d("ABC", "No data found for this user")
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("ABC", "Error fetching data: ${e.message}")
                }
        } else {
            Log.d("ABC", "User ID is null")
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ -> logout() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun logout() {
        val auth=FirebaseAuth.getInstance()
         auth.signOut()
        findNavController().navigate(
            R.id.action_profileFragment_to_loginFragment,
            null,
            Constant.slideRightLeftNavOptions
        )
    }
}
