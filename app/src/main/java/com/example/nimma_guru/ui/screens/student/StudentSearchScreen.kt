package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.R
import com.example.nimma_guru.ui.components.GuruCard
import com.example.nimma_guru.ui.viewmodel.GuruViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSearchScreen(
    viewModel: GuruViewModel,
    onGuruClick: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSkills by viewModel.selectedSkills.collectAsState()
    val filteredGurus by viewModel.gurus.collectAsState()
    
    val skills = listOf("Math", "Science", "Carpentry", "English", "Agriculture", "Music")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.search_hint)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(stringResource(R.string.filter_by_skill), style = MaterialTheme.typography.titleMedium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(skills) { skill ->
                FilterChip(
                    selected = skill in selectedSkills,
                    onClick = { viewModel.toggleSkill(skill) },
                    label = { Text(skill) }
                )
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredGurus) { guru ->
                GuruCard(guru = guru, onClick = { onGuruClick(guru.id) })
            }
        }
    }
}
