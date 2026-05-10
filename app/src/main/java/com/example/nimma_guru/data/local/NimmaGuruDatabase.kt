package com.example.nimma_guru.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nimma_guru.data.local.dao.UserDao
import com.example.nimma_guru.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class NimmaGuruDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
