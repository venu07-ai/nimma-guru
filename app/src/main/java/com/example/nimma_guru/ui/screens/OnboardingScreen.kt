package com.example.nimma_guru.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableStateOf(0) }
    
    val titles = listOf(
        "Welcome to Nimma-Guru",
        "Connect with Mentors",
        "Learn from Experts"
    )
    val descriptions = listOf(
        "A platform connecting village students with retired professionals.",
        "Find gurus in your own village for math, science, and more.",
        "Join weekend classes at your local Samudaya Bhavana."
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titles[step],
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = descriptions[step],
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                if (step < titles.size - 1) step++ else onFinish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (step < titles.size - 1) "Next" else "Get Started")
        }
    }
}
