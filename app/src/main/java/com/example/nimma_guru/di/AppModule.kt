package com.example.nimma_guru.di

import android.content.Context
import androidx.room.Room
import com.example.nimma_guru.data.local.NimmaGuruDatabase
import com.example.nimma_guru.data.local.dao.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NimmaGuruDatabase {
        return Room.databaseBuilder(
            context,
            NimmaGuruDatabase::class.java,
            "nimma_guru_db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: NimmaGuruDatabase): UserDao = db.userDao()
}
