package com.example.beautyparlorapp.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.FragmentDetailServiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class DetailServiceFragment : Fragment() {

    private lateinit var binding: FragmentDetailServiceBinding
    private lateinit var firestore:FirebaseFirestore
    private var serviceName:String?=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDetailServiceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore=FirebaseFirestore.getInstance()
        serviceName=arguments?.getString("serviceName")
        showBeauticianInfo()
        showServiceInfo()
        addToCart()

        binding.btnBookNow.setOnClickListener {
            Toast.makeText(requireContext(), "Ui is in Working Mode", Toast.LENGTH_SHORT).show()
           // findNavController().navigate(R.id.action_detailServiceFragment_to_appoinmentFragment)
        }
    }

    private fun addToCart() {
        binding.btnAddToCart.setOnClickListener {
            val serviceName = binding.serviceName.text.toString()
            val servicePrice = binding.servicePrice.text.toString()
            val serviceDuration = binding.serviceDuration.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "User is not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Adding to Cart....")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val cartCollection = firestore.collection("Cart Info")
            val documentId = cartCollection.document().id

            // Create ServiceModel object with both userId and documentId
            val cartData = ServiceModel(
                documentId = documentId,
                userId = userId,
                serviceName = serviceName,
                servicePrice = servicePrice,
                serviceDuration = serviceDuration
            ).apply { this.documentId = documentId }

            cartCollection.document(documentId)
                .set(cartData)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Successfully Added to Cart", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun showServiceInfo() {
        if (serviceName!=null){

            firestore.collection("Parlor Data Info")
                .whereEqualTo("serviceName",serviceName)
                .get()
                .addOnSuccessListener {document->
                    if (!document.isEmpty){
                        val doc=document.documents[0]
                        setUpServiceUI(doc)
                    }else{
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener { exception ->
                    Log.e("FirestoreData", "Error fetching data: ${exception.message}")
                }
        }else{
            Log.e("FirestoreData", "serviceName is null.")
        }
    }

    private fun setUpServiceUI(doc: DocumentSnapshot?){

        val serviceName=doc?.getString("serviceName")
        val serviceCategory=doc?.getString("serviceCategory")
        val serviceDescription=doc?.getString("serviceDescription")
        val serviceDuration=doc?.getString("serviceDuration")
        val servicePrice=doc?.getString("servicePrice")

        binding.service.text=serviceName
        binding.serviceCategory.text=serviceCategory
        binding.serviceName.text=serviceName
        binding.servicePrice.text="₹$servicePrice"
        binding.serviceDuration.text="$serviceDuration hrs 0 min"
        binding.serviceDesc.text=serviceDescription
    }

    private fun showBeauticianInfo() {

        if (serviceName!=null) {
            firestore.collection("Beautician Info")
                .whereEqualTo("serviceName", serviceName)
                .get()
                .addOnSuccessListener { document ->

                    if (!document.isEmpty) {
                        val doc = document.documents[0]
                        setUpBeauticianUI(doc)
                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Log.e("FirestoreData", "Error fetching data: ${exception.message}")
                }
        }else{
            Log.e("FirestoreData", "serviceName is null.")
        }
    }

    private fun setUpBeauticianUI(doc: DocumentSnapshot?) {
        // Extract data
        val beauticianName = doc?.getString("beauticianName") ?: "N/A"
        val beauticianAddress = doc?.getString("beauticianAddress") ?: "N/A"
        val beauticianRating = doc?.getString("beauticianRating") ?: "N/A"
        val beauticianReview = doc?.getString("beauticianReview") ?: "N/A"

        binding.beauticianName.text=beauticianName
        binding.beauticianAddress.text="⚲ $beauticianAddress"
        binding.beauticianRatingBar.rating=beauticianRating.toFloat()
        binding.beauticianReview.text="✍$beauticianReview"


    }

}