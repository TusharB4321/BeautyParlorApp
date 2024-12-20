package com.example.beautyparlorapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.BottomAddressDialogBinding
import com.example.beautyparlorapp.databinding.FragmentPaymentBinding
import com.example.beautyparlorapp.databinding.PaymentBottomSheetBinding
import com.example.beautyparlorapp.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentFragment : Fragment() {

    private lateinit var binding:FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentPaymentBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOrderDetails()
        setUpListener()

    }

    private fun setUpListener() {
        binding.paymentOption.setOnCheckedChangeListener { radioGroup,pos ->

            when(pos){
                R.id.online_payment->{
                    binding.btnPay.text="PAY SECURE ➪"
                }
                R.id.cash_payment->{
                    binding.btnPay.text="CONFIRM ➪"
                }
            }
        }

        binding.btnPay.setOnClickListener {

            when(binding.paymentOption.checkedRadioButtonId){
                R.id.online_payment->showBottomDialog()
                R.id.cash_payment->navigateToSuccessFragment()
                else-> Toast.makeText(requireContext(), "Please select Payment Mode", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun showBottomDialog() {
        val bottomSheetDialog=BottomSheetDialog(requireContext())
        val paymentBottomSheetBinding=PaymentBottomSheetBinding.inflate(layoutInflater)

        bottomSheetDialog.setContentView(paymentBottomSheetBinding.root)

        paymentBottomSheetBinding.btnPaySecure.setOnClickListener {
            
            val selectedMethodPaymentId=paymentBottomSheetBinding.paymentMethodGroup.checkedRadioButtonId
            
            if (selectedMethodPaymentId!=-1){

                val selectedPaymentMethod = when (selectedMethodPaymentId) {
                    R.id.radioPhonePe -> "PhonePe"
                    R.id.radioPaytm -> "Paytm"
                    R.id.radioGooglePay -> "Google Pay"
                    else -> "Unknown"
                }
                Toast.makeText(context, "Selected: $selectedPaymentMethod", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
                navigateToSuccessFragment()
            } else {
                Toast.makeText(context, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }

        }

        bottomSheetDialog.show()
    }

    private fun navigateToSuccessFragment() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        val cartCollection = FirebaseFirestore.getInstance().collection("Cart Info")
        cartCollection
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = FirebaseFirestore.getInstance().batch()
                for (doc in querySnapshot.documents) {
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Cart cleared successfully!", Toast.LENGTH_SHORT).show()
                        // Navigate to the success fragment
                        findNavController().navigate(
                            R.id.action_paymentFragment_to_successFragment,
                            null,
                            Constant.slideRightLeftNavOptions
                        )
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error clearing cart: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching cart: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    @SuppressLint("SetTextI18n")
    private fun getOrderDetails() {

        //order details
        val bookingDate=arguments?.getString("bookingDate")
        val bookingTime=arguments?.getString("bookingTime")
        val address=arguments?.getString("address")

        binding.boookingDate.text=bookingDate
        binding.boookingTime.text=bookingTime
        binding.boookingAddress.text=address

        // price details

        val price=arguments?.getDouble("totalPrice")
        val discount=arguments?.getDouble("discount")
        val totalItems=arguments?.getInt("totalItems")

        binding.tvItems.text="Price($totalItems item)"
        binding.boookingPrice.text="₹$price"
        binding.payableAmount.text="₹$price"
        binding.boookingDiscountPrice.text="-₹$discount"

        binding.boookingFinalAmount.text="₹"+(price!!- discount!!).toString()
        binding.tvAmount.text="₹"+(price- discount).toString()


    }

}