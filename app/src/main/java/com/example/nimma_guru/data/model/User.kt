package com.example.nimma_guru.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.NONE,
    val profilePhotoUrl: String = "",
    val village: String = "",
    val skills: List<String> = emptyList(),
    val experience: String = "",
    val availableHours: String = "",
    val languages: List<String> = emptyList(),
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val bio: String = "",
    val isVerified: Boolean = false
)
