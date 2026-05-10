package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.ui.components.GuruCard
import com.example.nimma_guru.ui.viewmodel.GuruViewModel

@Composable
fun StudentHomeScreen(viewModel: GuruViewModel) {
    val gurus by viewModel.gurus.collectAsState()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Welcome to Nimma-Guru", style = MaterialTheme.typography.headlineLarge)
            Text(text = "Find your perfect mentor today.", style = MaterialTheme.typography.titleMedium)
        }
        
        item {
            Text(text = "Community Announcements", style = MaterialTheme.typography.titleLarge)
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "New Woodworking Class!", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Join us this Sunday at Samudaya Bhavana.")
                }
            }
        }
        
        item {
            Text(text = "Featured Gurus", style = MaterialTheme.typography.titleLarge)
        }
        
        items(gurus.take(3)) { guru ->
            GuruCard(guru = guru, onClick = {})
        }
        
        item {
            Text(text = "Popular Subjects", style = MaterialTheme.typography.titleLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Math", "Science", "Carpentry", "Agriculture")) { subject ->
                    SuggestionChip(onClick = {}, label = { Text(subject) })
                }
            }
        }
    }
}
