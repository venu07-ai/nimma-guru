package com.example.nimma_guru.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nimma_guru.R
import com.example.nimma_guru.ui.viewmodel.SettingsViewModel
import com.example.nimma_guru.ui.viewmodel.AuthViewModel
import com.example.nimma_guru.ui.viewmodel.GuruViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun AdminProfileScreen(
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    guruViewModel: GuruViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUserProfile.collectAsState()
    var name by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            name = it.name
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
                "Admin Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Admin Name") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )
                    
                    Button(
                        onClick = {
                            currentUser?.let { current ->
                                val updatedUser = current.copy(name = name)
                                guruViewModel.saveGuru(updatedUser)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Admin profile updated!")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Name")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Settings Section
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
