package com.example.beautyparlorapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentLoginBinding
import com.example.beautyparlorapp.repository.UserRepository
import com.example.beautyparlorapp.utils.Constant
import com.example.beautyparlorapp.utils.Resources
import com.example.beautyparlorapp.viewmodel.UserViewModel
import com.example.beautyparlorapp.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val database= FirebaseDatabase.getInstance()
        val auth= FirebaseAuth.getInstance()
        val userRepository = UserRepository(firebaseAuth = auth, database = database)
        val viewModelFactory = ViewModelFactory(userRepository)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

        // Observe login status
        observeLoginStatus()

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

        binding.btnLoginApply.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            if (validateInputs(email, password)) {
                loginViewModel.loginUser(email, password)
            }
        }

    }

    private fun observeLoginStatus() {
        loginViewModel.status.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resources.Loading -> {
                    Toast.makeText(requireContext(), "Logging in...", Toast.LENGTH_SHORT).show()
                }
                is Resources.Success -> {
                    resource.data?.let {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        if (!Constant.isEmailValid(email) { showValidationError(it.message) }) {
            isValid = false
        }

        if (!Constant.isPasswordValid(password) { showValidationError(it.message) }) {
            isValid = false
        }

        return isValid
    }

    private fun showValidationError(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}