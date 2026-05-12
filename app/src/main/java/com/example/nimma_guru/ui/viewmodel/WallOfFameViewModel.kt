package com.example.nimma_guru.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimma_guru.data.model.Appreciation
import com.example.nimma_guru.data.repository.AppreciationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallOfFameViewModel @Inject constructor(
    private val repository: AppreciationRepository
) : ViewModel() {

    val appreciations: StateFlow<List<Appreciation>> = repository.getAllAppreciations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun postAppreciation(studentId: String, studentName: String, guruId: String, message: String) {
        viewModelScope.launch {
            val appreciation = Appreciation(
                studentId = studentId,
                studentName = studentName,
                guruId = guruId,
                message = message,
                timestamp = System.currentTimeMillis()
            )
            repository.postAppreciation(appreciation)
        }
    }
}
