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
import com.example.beautyparlorapp.databinding.FragmentCompletedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CompletedFragment : Fragment() {

    private lateinit var binding: FragmentCompletedBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var completedAdapter: UpcomingAdapter
    private val completedList = mutableListOf<UpcomingServiceModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(layoutInflater)
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        setUpRecyclerView()

        // Fetch completed services from Firestore
        fetchCompletedServices()

        return binding.root
    }

    private fun setUpRecyclerView() {
        completedAdapter = UpcomingAdapter(completedList)
        binding.completedRv.layoutManager = LinearLayoutManager(requireContext())
        binding.completedRv.adapter = completedAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCompletedServices() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Upcoming Services")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "Completed") // Filter by "Completed" status
            .addSnapshotListener { res, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                completedList.clear()  // Clear existing list
                for (doc in res!!) {
                    val service = doc.toObject(UpcomingServiceModel::class.java)
                    completedList.add(service)  // Add the fetched service to the list
                }
                completedAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
            }
    }
}
