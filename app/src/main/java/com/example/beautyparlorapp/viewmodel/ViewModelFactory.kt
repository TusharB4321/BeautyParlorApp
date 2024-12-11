package com.example.beautyparlorapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beautyparlorapp.repository.UserRepository

class ViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(userRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}