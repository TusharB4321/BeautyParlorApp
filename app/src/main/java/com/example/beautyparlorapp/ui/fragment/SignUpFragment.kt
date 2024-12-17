package com.example.beautyparlorapp.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentSignUpBinding
import com.example.beautyparlorapp.repository.UserRepository
import com.example.beautyparlorapp.utils.Constant
import com.example.beautyparlorapp.utils.Resources
import com.example.beautyparlorapp.viewmodel.UserViewModel
import com.example.beautyparlorapp.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment() {

    private lateinit var binding:FragmentSignUpBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (activity is AppCompatActivity) {
//            (activity as AppCompatActivity).supportActionBar!!.hide()
//        }
//        binding.toolbar.setTitle("AAAA")
//        binding.toolbar.setLogo(R.drawable.beauty_logo)
        // Initialize ViewModel
        val userRepository = UserRepository(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance())
        userViewModel = ViewModelProvider(this, ViewModelFactory(userRepository))[UserViewModel::class.java]

        val progress = ProgressDialog(requireContext())
        progress.setTitle("Please Wait...")
        progress.setCancelable(false)


        binding.tvLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.btnSignupApply.setOnClickListener {
            progress.show()
            val fullName = binding.etSignupName.text.toString()
            val email = binding.etSignupEmail.text.toString()
            val password = binding.etSignupPassword.text.toString()
            val confirmPass = binding.etSignupConfirmPassword.text.toString()
            val contactNumber = binding.etSignupNumber.text.toString()

            validateAndRegisterUser(fullName=fullName, email = email, password =  password,confirmPass=confirmPass, contactNumber = contactNumber, progress = progress)
        }

        // Observe ViewModel status
        userViewModel.status.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resources.Loading -> progress.show()
                is Resources.Success -> {
                    progress.dismiss()
                    findNavController().navigate(R.id.action_signUpFragment_to_homeFragment,null,Constant.slideRightLeftNavOptions)
                    Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                }
                is Resources.Error -> {
                    progress.dismiss()
                    Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun validateAndRegisterUser(
        fullName: String,
        email: String,
        password: String,
        confirmPass:String,
        contactNumber: String,
        progress: ProgressDialog
    ) {
        if (Constant.isNameValid(fullName) { showError(it.message) } &&
            Constant.isEmailValid(email) { showError(it.message) } &&
            Constant.isPasswordValid(password) { showError(it.message) } &&
            Constant.isNumberValid(contactNumber) { showError(it.message) }
            && Constant.isPasswordConfirmValid(password = password, confirmPass = confirmPass){showError(it.message)}
        ) {
            val userDetails = mapOf(
                "fullName" to fullName,
                "email" to email,
                "password" to password,
                "contactNumber" to contactNumber,
            )

            userViewModel.registerUser(email, password, userDetails)
        } else {
            progress.dismiss()
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}