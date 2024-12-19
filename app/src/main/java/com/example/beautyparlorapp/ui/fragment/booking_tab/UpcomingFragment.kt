package com.example.beautyparlorapp.ui.fragment.booking_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentUpcomingBinding


class UpcomingFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUpcomingBinding.inflate(layoutInflater)
        return binding.root
    }

}