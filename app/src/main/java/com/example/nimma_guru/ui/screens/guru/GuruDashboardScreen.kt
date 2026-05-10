package com.example.nimma_guru.ui.screens.guru

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.data.model.Session
import com.example.nimma_guru.data.model.SessionStatus

@Composable
fun GuruDashboardScreen() {
    // Mock pending requests
    val pendingSessions = remember {
        listOf(
            Session("1", "guru1", "Self", "stud1", "Anu", "Math", "2026-05-15", "10:00 AM"),
            Session("2", "guru1", "Self", "stud2", "Rahul", "Science", "2026-05-16", "04:00 PM")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Guru Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Pending Mentorship Requests", style = MaterialTheme.typography.titleLarge)
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(pendingSessions) { session ->
                SessionRequestCard(session)
            }
        }
    }
}

@Composable
fun SessionRequestCard(session: Session) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Request from: ${session.studentName}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Subject: ${session.subject}")
            Text(text = "Date: ${session.date} at ${session.time}")
            
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { /* Reject */ }) { Text("Reject") }
                Button(onClick = { /* Accept */ }) { Text("Accept") }
            }
        }
    }
}
