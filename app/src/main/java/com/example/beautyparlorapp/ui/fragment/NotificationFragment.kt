package com.example.beautyparlorapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.NotificationAdapter
import com.example.beautyparlorapp.databinding.FragmentNotificationBinding
import com.example.beautyparlorapp.ui.room.AppDatabase
import com.example.beautyparlorapp.ui.room.NotificationDao

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var notificationDao: NotificationDao
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNotificationBinding.bind(view)

        // Initialize Room database
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "app_database"
        ).build()

        notificationDao = db.notificationDao()

        Log.d("RoommDb",notificationDao.getAllNotifications().toString())

        // Set up RecyclerView
        binding.notificationRv.layoutManager = LinearLayoutManager(requireContext())

        // Observe notifications from the database
        notificationDao.getAllNotifications().observe(viewLifecycleOwner) { notifications ->
            notificationAdapter = NotificationAdapter(notifications)
            binding.notificationRv.adapter = notificationAdapter
            binding.notificationRv.setHasFixedSize(true)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding when view is destroyed
    }
}


