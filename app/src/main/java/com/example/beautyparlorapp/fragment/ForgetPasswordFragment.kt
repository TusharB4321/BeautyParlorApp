package com.example.beautyparlorapp.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentForgetPasswordBinding


class ForgetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentForgetPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnForgotPasswordSubmit.setOnClickListener {
            val emailOrPhone = binding.etForgotPasswordInput.text.toString().trim()

            if (emailOrPhone.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Email or Phone", Toast.LENGTH_SHORT).show()
            } else {
                binding.progressBar.visibility = View.VISIBLE
                sendResetLink(emailOrPhone)
            }
        }
    }

    private fun sendResetLink(emailOrPhone: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "Reset link sent to $emailOrPhone", Toast.LENGTH_LONG).show()
        }, 2000)
    }


}