package com.example.nimma_guru.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Appreciation(
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val guruId: String = "",
    val message: String = "",
    val rating: Int = 5,
    val successStory: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
