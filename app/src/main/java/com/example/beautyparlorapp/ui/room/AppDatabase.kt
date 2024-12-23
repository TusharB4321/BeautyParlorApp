package com.example.beautyparlorapp.ui.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotificationEntity::class], version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun notificationDao():NotificationDao

}