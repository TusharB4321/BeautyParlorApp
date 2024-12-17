package com.example.beautyparlorapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val databaseRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser?.uid != null) {
            showProfileFields()
        } else {
            Log.d("EditProfileFragment", "UserId is null")
        }
        initListeners()
    }

    private fun initListeners() {
        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    private fun showProfileFields() {
        databaseRef.get()
            .addOnSuccessListener { snapshot ->
                snapshot.takeIf { it.exists() }?.let { res ->
                    binding.etProfileName.setText(res.child("fullName").value?.toString())
                    binding.etProfileNumber.setText(res.child("contactNumber").value?.toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile() {
        val fullName = binding.etProfileName.text.toString().trim()
        val contactNumber = binding.etProfileNumber.text.toString().trim()

        if (fullName.isEmpty() || contactNumber.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        showProgressBar()

        val updatedData = mapOf(
            "fullName" to fullName,
            "contactNumber" to contactNumber
        )

        databaseRef.updateChildren(updatedData)
            .addOnSuccessListener {
                onProfileUpdateSuccess()
            }
            .addOnFailureListener { e ->
                onProfileUpdateFailure(e)
            }
    }

    private fun showProgressBar() {
        binding.progressBarUpdate.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarUpdate.visibility = View.GONE
    }

    private fun onProfileUpdateSuccess() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            hideProgressBar()
            findNavController().navigate(
                R.id.action_editProfileFragment_to_profileFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.editProfileFragment, true).build()
            )
        }
        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun onProfileUpdateFailure(e: Exception) {
        hideProgressBar()
        Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
