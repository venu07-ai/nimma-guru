package com.example.nimma_guru.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimma_guru.data.model.Session
import com.example.nimma_guru.data.repository.SessionRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: SessionRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val sessions: StateFlow<List<Session>> = repository.getUpcomingSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun scheduleSession(subject: String, date: String, time: String, location: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val session = Session(
                guruId = currentUser.uid,
                guruName = currentUser.displayName ?: "Guru",
                subject = subject,
                date = date,
                time = time,
                location = location
            )
            viewModelScope.launch {
                repository.scheduleSession(session)
            }
        }
    }
}
