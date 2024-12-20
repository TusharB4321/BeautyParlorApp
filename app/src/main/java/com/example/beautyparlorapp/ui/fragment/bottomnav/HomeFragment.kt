package com.example.beautyparlorapp.ui.fragment.bottomnav
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.ServiceAdapter
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.FragmentHomeBinding
import com.example.beautyparlorapp.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var categoryList: ArrayList<ServiceModel> // This will hold the category names
    private val serviceAdapter by lazy {
        ServiceAdapter(onProductClick = ::onCategoryClick, context = requireContext(), list = categoryList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        displayName()
        slideImages()
        setUpRecyclerView()

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment,null,Constant.slideRightLeftNavOptions)
        }
    }

    private fun setUpRecyclerView() {
        categoryList = ArrayList() // Initialize category list
        binding.serviceRv.adapter = serviceAdapter
        binding.serviceRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.serviceRv.setHasFixedSize(true)

        fetchServiceCategories()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchServiceCategories() {
        val firestore=FirebaseFirestore.getInstance()
        firestore.collection("Parlor Data Info")
            .get()
            .addOnSuccessListener { doc->
                categoryList.clear()
                val uniqueCategories = mutableSetOf<String>() // Set to track unique categories

                for (data in doc) {
                    val service = data.toObject(ServiceModel::class.java)

                    // Check if the category is unique before adding
                    if (!uniqueCategories.contains(service.serviceCategory)) {
                        uniqueCategories.add(service.serviceCategory!!)
                        categoryList.add(service) // Add to list to display in RecyclerView
                    }
                }
                serviceAdapter.notifyDataSetChanged()
            }
    }

    private fun displayName() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)

            userRef.child("fullName").get().addOnSuccessListener { res ->
                val username = res.getValue(String::class.java)
                if (username != null) {
                    binding.userName.text = username
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Error fetching username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun slideImages() {
        val imgList = ArrayList<SlideModel>()
        imgList.add(SlideModel(R.drawable.ser3))
        imgList.add(SlideModel(R.drawable.ser4))
        imgList.add(SlideModel(R.drawable.ser5))
        imgList.add(SlideModel(R.drawable.ser3))


        binding.imageSlider.setImageList(imgList)
        binding.imageSlider.setImageList(imgList, ScaleTypes.CENTER_CROP)
    }

    private fun onCategoryClick(category: ServiceModel) {
        val bundle = Bundle().apply {
            putString("serviceCategory", category.serviceCategory) // Pass the selected category to the next fragment
            //putInt("serviceImage", imgRes)
        }
        findNavController().navigate(R.id.action_homeFragment_to_serviceListFragment, bundle,Constant.slideRightLeftNavOptions)
    }
}
