package com.example.nimma_guru.ui.screens.guru

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimma_guru.R
import com.example.nimma_guru.data.model.User
import com.example.nimma_guru.data.model.UserRole
import com.example.nimma_guru.ui.viewmodel.GuruViewModel

@Composable
fun GuruProfileScreen(viewModel: GuruViewModel) {
    var name by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.edit_guru_profile), style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = village,
            onValueChange = { village = it },
            label = { Text(stringResource(R.string.village_street)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skills,
            onValueChange = { skills = it },
            label = { Text(stringResource(R.string.skills_hint)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Math, Science, Carpentry") }
        )

        OutlinedTextField(
            value = hours,
            onValueChange = { hours = it },
            label = { Text(stringResource(R.string.free_hours_hint)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Saturday 10 AM - 12 PM") }
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text(stringResource(R.string.bio)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                val user = User(
                    name = name,
                    village = village,
                    skills = skills.split(",").map { it.trim() },
                    availableHours = hours,
                    bio = bio,
                    role = UserRole.GURU
                )
                viewModel.saveGuru(user)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_profile))
        }
    }
}
