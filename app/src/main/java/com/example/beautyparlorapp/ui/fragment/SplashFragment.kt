package com.example.beautyparlorapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.FragmentSplashBinding
import com.example.beautyparlorapp.utils.Constant
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user=FirebaseAuth.getInstance().currentUser
       //  Hide the ActionBar
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.hide()
        }
        Handler(Looper.getMainLooper()).postDelayed({

            if (user != null) {
                findNavController().navigate(
                    R.id.action_splashFragment_to_homeFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setEnterAnim(Constant.slideRightLeftNavOptions.enterAnim)
                        .setExitAnim(Constant.slideRightLeftNavOptions.exitAnim)
                        .setPopUpTo(R.id.splashFragment, true) // Remove splashFragment from back stack
                        .build()
                )
            } else {
                findNavController().navigate(
                    R.id.action_splashFragment_to_signUpFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setEnterAnim(Constant.slideRightLeftNavOptions.enterAnim)
                        .setExitAnim(Constant.slideRightLeftNavOptions.exitAnim)
                        .setPopUpTo(R.id.splashFragment, true) // Remove splashFragment from back stack
                        .build()
                )
            }

        },3000)
    }



}