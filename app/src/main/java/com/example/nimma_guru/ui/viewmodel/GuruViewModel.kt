package com.example.nimma_guru.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimma_guru.data.model.User
import com.example.nimma_guru.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuruViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedSkills = MutableStateFlow(setOf<String>())
    val selectedSkills: StateFlow<Set<String>> = _selectedSkills

    val gurus: StateFlow<List<User>> = combine(
        userRepository.getGurus(),
        _searchQuery,
        _selectedSkills
    ) { gurus, query, skills ->
        gurus.filter { guru ->
            (query.isEmpty() || guru.name.contains(query, ignoreCase = true) || guru.village.contains(query, ignoreCase = true)) &&
            (skills.isEmpty() || guru.skills.any { it in skills })
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            userRepository.fetchGurusFromRemote()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSkill(skill: String) {
        _selectedSkills.value = if (skill in _selectedSkills.value) {
            _selectedSkills.value - skill
        } else {
            _selectedSkills.value + skill
        }
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedSkills.value = emptySet()
    }

    fun saveGuru(user: User) {
        viewModelScope.launch {
            userRepository.saveUserProfile(user)
        }
    }
}
