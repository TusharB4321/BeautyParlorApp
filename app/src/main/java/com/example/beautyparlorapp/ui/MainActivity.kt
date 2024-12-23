package com.example.beautyparlorapp.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setSupportActionBar(binding.toolbar)
         actionBar?.hide()


        navigateFragment()
    }

    private fun navigateFragment() {
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController=navHostFragment.navController
        setupActionBarWithNavController(navController)

        navHostFragment.navController.addOnDestinationChangedListener{_, destination, _ ->

            when(destination.id){
                R.id.splashFragment,
                R.id.signUpFragment,
                R.id.loginFragment,
                R.id.searchFragment,
                R.id.editProfileFragment,
                R.id.detailServiceFragment,
                R.id.serviceListFragment,
                R.id.appointmentFragment,
                R.id.addressFragment,
                R.id.paymentFragment,
                R.id.successFragment,
                R.id.forgetPasswordFragment,
                R.id.notificationFragment,
                    ->{
                    binding.bottomBar.visibility= View.GONE
                }
                else->{
                    binding.bottomBar.visibility= View.VISIBLE
                }
            }

        }
        // Handle Bottom Navigation item selection
        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.cart ->{
                    navController.navigate(R.id.cartFragment)
                    true
                }
                R.id.booking ->{
                    navController.navigate(R.id.bookingFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}