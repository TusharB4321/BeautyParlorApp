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
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userId=FirebaseAuth.getInstance().currentUser?.uid
        if (userId!=null){
             databaseRef=FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
            showProfileFields()
        }else{
            Log.d("ABC","UserId is null")
        }
        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    private fun showProfileFields() {
        databaseRef.get().addOnSuccessListener { res ->
            if (res.exists()) {
                val fullName = res.child("fullName").value?.toString() ?: ""
                val email = res.child("email").value?.toString() ?: ""
                val contactNumber = res.child("contactNumber").value?.toString() ?: ""
                binding.etProfileName.setText(fullName)
                binding.etProfileEmail.setText(email)
                binding.etProfileNumber.setText(contactNumber)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Error fetching profile: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfile() {

        val fullName=binding.etProfileName.text.toString().trim()
        val contactNumber=binding.etProfileNumber.text.toString().trim()
        val email=binding.etProfileEmail.text.toString().trim()
        
        if (fullName.isEmpty()||contactNumber.isEmpty()||email.isEmpty()){
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

         binding.progressBarUpdate.visibility = View.VISIBLE // Show ProgressBar

        val updatedData = mapOf(
            "fullName" to fullName,
            "email" to email,
            "contactNumber" to contactNumber
        )

        databaseRef.updateChildren(updatedData)
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000) // Wait for 2 seconds
                    binding.progressBarUpdate.visibility=View.GONE
                    findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                }
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}