package com.example.beautyparlorapp.data

data class NotificationItem(
    val title: String,
    val body: String,
    val imageUrl: String? = null // Image URL can be null if not provided
)