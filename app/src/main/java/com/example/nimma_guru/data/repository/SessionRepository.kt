package com.example.nimma_guru.data.repository

import com.example.nimma_guru.data.model.Session
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val sessionsCollection = firestore.collection("sessions")

    fun getUpcomingSessions(): Flow<List<Session>> {
        return sessionsCollection
            .orderBy("date")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(Session::class.java) }
    }

    fun getGuruSessions(guruId: String): Flow<List<Session>> {
        return sessionsCollection
            .whereEqualTo("guruId", guruId)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(Session::class.java) }
    }

    suspend fun scheduleSession(session: Session) {
        val docRef = sessionsCollection.document()
        val sessionWithId = session.copy(id = docRef.id)
        docRef.set(sessionWithId).await()
    }

    suspend fun updateSessionStatus(sessionId: String, status: com.example.nimma_guru.data.model.SessionStatus) {
        sessionsCollection.document(sessionId).update("status", status).await()
    }
}
