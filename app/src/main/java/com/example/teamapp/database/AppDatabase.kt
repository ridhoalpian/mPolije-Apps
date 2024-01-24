package com.example.teamapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.teamapp.model.ResponseUserGithub

@Database(entities = [ResponseUserGithub.Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
