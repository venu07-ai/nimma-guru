package com.example.nimma_guru.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nimma_guru.data.model.User
import com.example.nimma_guru.data.model.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val role: String,
    val profilePhotoUrl: String,
    val village: String,
    val skills: String, // Comma separated
    val experience: String,
    val availableHours: String,
    val languages: String, // Comma separated
    val rating: Float,
    val isVerified: Boolean
)

fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    role = role.name,
    profilePhotoUrl = profilePhotoUrl,
    village = village,
    skills = skills.joinToString(","),
    experience = experience,
    availableHours = availableHours,
    languages = languages.joinToString(","),
    rating = rating,
    isVerified = isVerified
)

fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    email = email,
    role = UserRole.valueOf(role),
    profilePhotoUrl = profilePhotoUrl,
    village = village,
    skills = if (skills.isEmpty()) emptyList() else skills.split(","),
    experience = experience,
    availableHours = availableHours,
    languages = if (languages.isEmpty()) emptyList() else languages.split(","),
    rating = rating,
    isVerified = isVerified
)
