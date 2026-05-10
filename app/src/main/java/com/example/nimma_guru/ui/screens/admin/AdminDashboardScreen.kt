package com.example.nimma_guru.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.data.model.User
import com.example.nimma_guru.data.model.UserRole

@Composable
fun AdminDashboardScreen() {
    val unverifiedGurus = remember {
        listOf(
            User("3", "Basavaraj", "basu@test.com", UserRole.GURU, village = "Village C", bio = "Retd. Scientist"),
            User("4", "Gowramma", "gowri@test.com", UserRole.GURU, village = "Village D", bio = "Expert Weaver")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Admin Panel", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Verify Gurus", style = MaterialTheme.typography.titleLarge)
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(unverifiedGurus) { guru ->
                VerificationCard(guru)
            }
        }
    }
}

@Composable
fun VerificationCard(guru: User) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = guru.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Village: ${guru.village}")
            Text(text = "Bio: ${guru.bio}")
            
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { /* Verify */ }) { Text("Verify Profile") }
            }
        }
    }
}
