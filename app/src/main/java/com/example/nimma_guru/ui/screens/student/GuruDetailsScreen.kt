package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nimma_guru.data.model.User

@Composable
fun GuruDetailsScreen(guru: User, onBookClick: () -> Unit, onAppreciateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = guru.profilePhotoUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.Crop
        )
        
        Text(text = guru.name, style = MaterialTheme.typography.headlineLarge)
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
            Text(text = "${guru.rating} (${guru.reviewCount} reviews)", style = MaterialTheme.typography.bodyLarge)
        }

        Text(text = "About Me", style = MaterialTheme.typography.titleLarge)
        Text(text = guru.bio)

        Text(text = "Skills & Subjects", style = MaterialTheme.typography.titleLarge)
        Text(text = guru.skills.joinToString(", "))

        Text(text = "Experience", style = MaterialTheme.typography.titleLarge)
        Text(text = guru.experience)

        Text(text = "Availability", style = MaterialTheme.typography.titleLarge)
        Text(text = guru.availableHours)

        Text(text = "Location", style = MaterialTheme.typography.titleLarge)
        Text(text = guru.village)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onBookClick, modifier = Modifier.weight(1f)) {
                Text("Book Session")
            }
            OutlinedButton(onClick = onAppreciateClick, modifier = Modifier.weight(1f)) {
                Text("Appreciate")
            }
        }
    }
}
