package com.example.beautyparlorapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.ServiceAdapter
import com.example.beautyparlorapp.adapter.ServiceListAdapter
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.FragmentServiceListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class ServiceListFragment : Fragment() {

    private lateinit var binding: FragmentServiceListBinding
    private lateinit var serviceList:ArrayList<ServiceModel>
    private val serviceListAdapter by lazy { ServiceListAdapter(onProductClick = ::onProductClick,requireContext(),list=serviceList) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentServiceListBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        serviceList=ArrayList()

        binding.serviceListRv.adapter=serviceListAdapter
        binding.serviceListRv.layoutManager=LinearLayoutManager(requireContext())
        binding.serviceListRv.setHasFixedSize(true)
        fetchListServices()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchListServices() {
        val firestoreDatabase = FirebaseFirestore.getInstance()

        // Get category passed from HomeFragment
        val serviceCategory = arguments?.getString("serviceCategory")
        if (serviceCategory != null) {
            firestoreDatabase.collection("Parlor Data Info")
                .whereEqualTo("serviceCategory", serviceCategory) // Filter by category
                .get()
                .addOnSuccessListener { res ->
                    serviceList.clear()
                    for (document in res) {
                        val service = document.toObject(ServiceModel::class.java)
                        serviceList.add(service)
                    }
                    serviceListAdapter.notifyDataSetChanged() // Notify adapter about data change
                }
                .addOnFailureListener { e ->
                    Log.e("CategoryFragment", "Error fetching data: ${e.message}")
                }
        } else {
            Log.e("CategoryFragment", "No category passed from HomeFragment!")
        }
    }

    private fun onProductClick(serviceModel: ServiceModel) {
       findNavController().navigate(R.id.action_serviceListFragment_to_detailServiceFragment)
    }

}