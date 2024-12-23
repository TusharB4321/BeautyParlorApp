package com.example.beautyparlorapp.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.Room
import com.example.beautyparlorapp.ui.room.AppDatabase
import com.example.beautyparlorapp.ui.room.NotificationEntity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationDao by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration() // Handle database migrations
            .build()
            .notificationDao()
    }

    override fun onCreate() {
        super.onCreate()

        // Fetch the current token when the service is created (if needed)
        fetchFirebaseToken()
    }

    private fun fetchFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get the new FCM registration token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
            // Send the token to your backend server or use it for push notifications
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM message
        val title = remoteMessage.notification?.title ?: "No title"
        val message = remoteMessage.notification?.body ?: "No message"
        val timestamp = System.currentTimeMillis()

        val notification = NotificationEntity(
            title = title,
            message = message,
            timestamp = timestamp
        )

        // Save notification to the Room database on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationDao.insert(notification)
            } catch (e: Exception) {
                // Handle potential errors during insertion
                e.printStackTrace()
                Log.e("FCM Error", "Error saving notification to database", e)
            }
        }
    }
}


