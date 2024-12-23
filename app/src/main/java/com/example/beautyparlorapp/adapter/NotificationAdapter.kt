package com.example.beautyparlorapp.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyparlorapp.databinding.ItemNotificationBinding
import com.example.beautyparlorapp.ui.room.NotificationEntity

class NotificationAdapter(private val notifications: List<NotificationEntity>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.binding.apply {
            notificationTitle.text = notification.title
            notificationMessage.text = notification.message
            notificationTimestamp.text = DateUtils.getRelativeTimeSpanString(notification.timestamp)
        }
    }

    override fun getItemCount(): Int = notifications.size

    class NotificationViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)
}

