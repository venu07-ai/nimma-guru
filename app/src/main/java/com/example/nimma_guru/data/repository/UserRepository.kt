package com.example.nimma_guru.data.repository

import com.example.nimma_guru.data.local.dao.UserDao
import com.example.nimma_guru.data.local.entity.toDomain
import com.example.nimma_guru.data.local.entity.toEntity
import com.example.nimma_guru.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) {
    private val usersCollection = firestore.collection("users")

    fun getGurus(): Flow<List<User>> = userDao.getGurus().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun fetchGurusFromRemote() {
        val snapshot = usersCollection.whereEqualTo("role", "GURU").get().await()
        val gurus = snapshot.toObjects(User::class.java)
        userDao.insertUsers(gurus.map { it.toEntity() })
    }

    suspend fun getUserProfile(userId: String): User? {
        // Try local first
        val local = userDao.getUserById(userId)?.toDomain()
        if (local != null) return local

        // Else fetch remote
        return try {
            val doc = usersCollection.document(userId).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUserProfile(user: User) {
        usersCollection.document(user.id).set(user).await()
        userDao.insertUsers(listOf(user.toEntity()))
    }
}
