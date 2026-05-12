package com.example.nimma_guru.data.repository

import com.example.nimma_guru.data.model.Appreciation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppreciationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val appreciationsCollection = firestore.collection("appreciations")

    fun getAllAppreciations(): Flow<List<Appreciation>> {
        return appreciationsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { it.toObjects(Appreciation::class.java) }
    }

    suspend fun postAppreciation(appreciation: Appreciation) {
        appreciationsCollection.add(appreciation).await()
    }
}
