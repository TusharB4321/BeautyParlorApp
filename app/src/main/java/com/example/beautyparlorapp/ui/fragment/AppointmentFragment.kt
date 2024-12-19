package com.example.beautyparlorapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentBinding
    private var selectedTextView: TextView? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var currentAddress:String?=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAppointmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListenerUI()
        auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference("users")

        fetchAddress()



    }
    private fun fetchAddress() {
        // Fetch existing address from Firebase
        val userId=auth.currentUser?.uid

        if (userId!=null){
            databaseReference.child(userId)
                .child("address")
                .get()
                .addOnSuccessListener { res->
                    currentAddress=res.getValue(String::class.java)
                    if (currentAddress!=null){
                        binding.etAddress.setText(currentAddress)
                        binding.tvaddAddress.text="Update Address"
                    }else{
                        Log.d("Addresss","currentAddress is Null")
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Error fetching username", Toast.LENGTH_SHORT).show()
                }
        }else{
            Log.d("Addresss","userId is Null")
        }

    }

    private fun clickListenerUI() {
        binding.calenderDatePicker.getSelectedDate(OnDateSelectedListener { selectedDate ->
            if (selectedDate != null) {
                //handleDateSelection(selectedDate)
                binding.calenderDatePicker.setBackgroundResource(R.drawable.time_selected_background)
                Toast.makeText(requireContext(), selectedDate.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        binding.tvaddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentFragment_to_addressFragment,null,Constant.slideRightLeftNavOptions)
        }

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "Working Mode", Toast.LENGTH_SHORT).show()
           // findNavController().navigate(R.id.action_appointmentFragment_to_paymentFragment,null,Constant.slideRightLeftNavOptions)
        }

        gridLayoutLogic()
    }

    private fun gridLayoutLogic() {
        for (i in 0 until binding.gridLayout.childCount){

            val child=binding.gridLayout.getChildAt(i)

            if (child is TextView){
                child.setOnClickListener {
                    binding.calenderDatePicker.setBackgroundColor(Color.WHITE)
                    handleTimeSelection(child)
                }
            }

        }
    }

    private fun handleTimeSelection(selectedView: TextView) {
        // Reset the background of the previously selected TextView
        selectedTextView?.setBackgroundResource(R.drawable.time_background)

        // Change the background of the newly selected TextView
        selectedView.setBackgroundResource(R.drawable.time_selected_background)

        // Show a toast message with the selected time
        Toast.makeText(requireContext(), "Selected time: ${selectedView.text}", Toast.LENGTH_SHORT).show()

        // Update the selected TextView reference
        selectedTextView = selectedView
    }

}