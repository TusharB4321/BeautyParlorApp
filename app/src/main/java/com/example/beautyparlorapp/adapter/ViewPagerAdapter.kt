package com.example.beautyparlorapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.beautyparlorapp.ui.fragment.booking_tab.CompletedFragment
import com.example.beautyparlorapp.ui.fragment.booking_tab.UpcomingFragment

class ViewPagerAdapter(fa:FragmentActivity):FragmentStateAdapter(fa){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->UpcomingFragment()
            1->CompletedFragment()

            else->UpcomingFragment()
        }
    }
}