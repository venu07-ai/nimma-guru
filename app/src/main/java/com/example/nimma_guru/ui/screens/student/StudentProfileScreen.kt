package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.R
import com.example.nimma_guru.data.model.User
import com.example.nimma_guru.data.model.UserRole
import com.example.nimma_guru.ui.viewmodel.GuruViewModel
import com.example.nimma_guru.ui.viewmodel.SettingsViewModel
import com.example.nimma_guru.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun StudentProfileScreen(
    viewModel: GuruViewModel,
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel
) {
    val currentUser by authViewModel.currentUserProfile.collectAsState()

    var name by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            name = it.name
            village = it.village
            interests = it.skills.joinToString(", ")
            bio = it.bio
        }
    }

    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val language by settingsViewModel.language.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Student Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Profile Form Card
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StudentProfileField(value = name, onValueChange = { name = it }, label = stringResource(R.string.name), icon = Icons.Default.Person)
                    StudentProfileField(value = village, onValueChange = { village = it }, label = stringResource(R.string.village_street), icon = Icons.Default.Home)
                    StudentProfileField(value = interests, onValueChange = { interests = it }, label = "Learning Interests", icon = Icons.AutoMirrored.Filled.MenuBook, placeholder = "e.g. Math, Science, Arts")
                    StudentProfileField(value = bio, onValueChange = { bio = it }, label = stringResource(R.string.bio), icon = Icons.Default.Info, singleLine = false, minLines = 3)
                }
            }

            Button(
                onClick = {
                    currentUser?.let { current ->
                        val updatedUser = current.copy(
                            name = name,
                            village = village,
                            skills = interests.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            bio = bio
                        )
                        viewModel.saveGuru(updatedUser)
                        scope.launch {
                            snackbarHostState.showSnackbar("Profile updated successfully!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.save_profile), fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Settings Section (Same as Guru)
            Text(
                stringResource(R.string.app_settings),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Language Switcher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(stringResource(R.string.language), style = MaterialTheme.typography.bodyLarge)
                        }
                        
                        Row {
                            FilterChip(
                                selected = language == "en",
                                onClick = { settingsViewModel.setLanguage("en") },
                                label = { Text(stringResource(R.string.english)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = language == "kn",
                                onClick = { settingsViewModel.setLanguage("kn") },
                                label = { Text(stringResource(R.string.kannada)) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Theme Switcher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isDarkTheme == true) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(stringResource(R.string.theme), style = MaterialTheme.typography.bodyLarge)
                        }

                        Switch(
                            checked = isDarkTheme == true,
                            onCheckedChange = { settingsViewModel.toggleTheme(it) },
                            thumbContent = {
                                Icon(
                                    if (isDarkTheme == true) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentProfileField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = placeholder?.let { { Text(it) } },
        singleLine = singleLine,
        minLines = minLines,
        shape = MaterialTheme.shapes.medium
    )
}
