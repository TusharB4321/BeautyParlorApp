package com.example.beautyparlorapp.ui.fragment.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.ViewPagerAdapter
import com.example.beautyparlorapp.databinding.FragmentBookingBinding
import com.google.android.material.tabs.TabLayoutMediator


class BookingFragment : Fragment() {

    private lateinit var binding:FragmentBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentBookingBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         binding.viewPager.adapter=ViewPagerAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,pos->

            when(pos){
                0->tab.text="Upcoming"
                1->tab.text="Completed"
            }

        }.attach()
    }

}