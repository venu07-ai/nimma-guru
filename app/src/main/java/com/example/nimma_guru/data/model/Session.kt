package com.example.nimma_guru.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: String = "",
    val guruId: String = "",
    val guruName: String = "",
    val studentId: String? = null,
    val studentName: String? = null,
    val subject: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "Samudaya Bhavana",
    val status: SessionStatus = SessionStatus.PENDING,
    val materials: List<String> = emptyList(),
    val notes: String = "",
    val attendance: List<String> = emptyList() // List of student IDs
)

enum class SessionStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    COMPLETED
}
