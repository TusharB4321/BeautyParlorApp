package com.example.beautyparlorapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentSuccessBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class SuccessFragment : Fragment() {

    private lateinit var binding:FragmentSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentSuccessBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.successAnimation.playAnimation()

        binding.backToHome.setOnClickListener {
            // Navigate to HomeFragment
            findNavController().navigate(R.id.action_successFragment_to_homeFragment)
            // Update the BottomNavigationView to select the Home icon
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomBar) // Replace with your BottomNavigationView ID
            bottomNav.selectedItemId = R.id.home // Replace with the item ID for the home fragment in the BottomNavigationView
        }
    }

}