package com.example.beautyparlorapp.ui.fragment.booking_tab

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.UpcomingAdapter
import com.example.beautyparlorapp.data.UpcomingServiceModel
import com.example.beautyparlorapp.databinding.FragmentUpcomingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UpcomingFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var upcomingAdapter: UpcomingAdapter
    private val upcomingList = mutableListOf<UpcomingServiceModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpcomingBinding.inflate(layoutInflater)
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        setUpRecyclerView()

        // Fetch upcoming services from Firestore
        fetchUpcomingServices()

        return binding.root
    }

    private fun setUpRecyclerView() {
        upcomingAdapter = UpcomingAdapter(upcomingList)
        binding.upcomingRv.layoutManager = LinearLayoutManager(requireContext())
        binding.upcomingRv.adapter = upcomingAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchUpcomingServices() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Upcoming Services")
            .whereEqualTo("userId", userId)  // Filter services by user ID
            .get()
            .addOnSuccessListener { res ->
                upcomingList.clear()  // Clear existing list
                for (doc in res) {
                    val service = doc.toObject(UpcomingServiceModel::class.java)
                    upcomingList.add(service)  // Add the fetched service to the list
                }
                upcomingAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
