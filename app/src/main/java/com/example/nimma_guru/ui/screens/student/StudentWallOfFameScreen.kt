package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.R
import com.example.nimma_guru.data.model.Appreciation

@Composable
fun StudentWallOfFameScreen() {
    val appreciations = remember {
        listOf(
            Appreciation("1", "Anu", "1", "Thank you Ramesh Sir for the math class!"),
            Appreciation("2", "Rahul", "2", "Learned how to fix a chair, thanks Suresh Uncle!")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.wall_of_fame), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.community_heart), style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(appreciations) { note ->
                AppreciationCard(note)
            }
        }
    }
}

@Composable
fun AppreciationCard(note: Appreciation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "From: ${note.studentName}", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "\"${note.message}\"", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
