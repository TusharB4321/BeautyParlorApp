package com.example.beautyparlorapp.data

data class UpcomingServiceModel(
    val serviceName: String = "",
    val bookingDate: String = "",
    val bookingTime: String = "",
    val status: String = "Pending", // Default status is "Pending"
    val userId: String = "",  // User ID to associate with the user
)
