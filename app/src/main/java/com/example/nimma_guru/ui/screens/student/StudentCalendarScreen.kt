package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.R
import com.example.nimma_guru.data.model.Session

@Composable
fun StudentCalendarScreen() {
    val sessions = remember {
        listOf(
            Session("1", "1", "Ramesh Kumar", "Algebra", "2026-05-10", "10:00 AM"),
            Session("2", "2", "Suresh Hegde", "Woodwork Basics", "2026-05-11", "04:00 PM")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.upcoming_sessions), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sessions) { session ->
                SessionCard(session)
            }
        }
    }
}

@Composable
fun SessionCard(session: Session) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = session.subject, style = MaterialTheme.typography.titleLarge)
            Text(text = "Guru: ${session.guruName}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Date: ${session.date} | Time: ${session.time}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Location: ${session.location}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
