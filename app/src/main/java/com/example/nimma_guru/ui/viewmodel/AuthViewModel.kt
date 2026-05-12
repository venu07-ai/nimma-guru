package com.example.nimma_guru.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimma_guru.data.model.UserRole
import com.example.nimma_guru.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUserRole = MutableStateFlow<UserRole>(UserRole.NONE)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole

    private val _currentUserProfile = MutableStateFlow<com.example.nimma_guru.data.model.User?>(null)
    val currentUserProfile: StateFlow<com.example.nimma_guru.data.model.User?> = _currentUserProfile

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = auth.currentUser
        if (user != null) {
            viewModelScope.launch {
                fetchUserRole(user.uid)
            }
        }
    }

    private suspend fun fetchUserRole(uid: String) {
        try {
            val userProfile = userRepository.getUserProfile(uid)
            if (userProfile != null) {
                _currentUserProfile.value = userProfile
                _currentUserRole.value = userProfile.role
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error("User profile not found")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Failed to fetch role")
        }
    }

    fun signUp(email: String, pass: String, role: UserRole, name: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                result.user?.let {
                    val user = com.example.nimma_guru.data.model.User(
                        id = it.uid,
                        name = name,
                        email = email,
                        role = role
                    )
                    userRepository.saveUserProfile(user)
                    _currentUserRole.value = role
                    _authState.value = AuthState.Authenticated
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, pass: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, pass).await()
                result.user?.let {
                    fetchUserRole(it.uid)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUserProfile.value = null
        _currentUserRole.value = UserRole.NONE
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
