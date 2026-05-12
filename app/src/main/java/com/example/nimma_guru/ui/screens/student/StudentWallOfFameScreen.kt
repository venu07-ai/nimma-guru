package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nimma_guru.R
import com.example.nimma_guru.data.model.Appreciation
import com.example.nimma_guru.data.model.UserRole
import com.example.nimma_guru.ui.viewmodel.AuthViewModel
import com.example.nimma_guru.ui.viewmodel.WallOfFameViewModel
import com.example.nimma_guru.ui.viewmodel.GuruViewModel

@Composable
fun StudentWallOfFameScreen(
    viewModel: WallOfFameViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    guruViewModel: GuruViewModel = hiltViewModel()
) {
    val appreciations by viewModel.appreciations.collectAsState()
    val userRole by authViewModel.currentUserRole.collectAsState()
    val currentUser by authViewModel.currentUserProfile.collectAsState()
    val gurus by guruViewModel.gurus.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (userRole == UserRole.STUDENT) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Post Appreciation")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.wall_of_fame),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.community_heart),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic
                )
            }

            if (appreciations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No stories shared yet. Be the first!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(appreciations) { note ->
                        AdvancedAppreciationCard(note)
                    }
                }
            }
        }
    }

    if (showAddDialog && currentUser != null) {
        PostAppreciationDialog(
            gurus = gurus,
            onDismiss = { showAddDialog = false },
            onConfirm = { guruId, message ->
                viewModel.postAppreciation(
                    studentId = currentUser!!.id,
                    studentName = currentUser!!.name,
                    guruId = guruId,
                    message = message
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AdvancedAppreciationCard(note: Appreciation) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = note.studentName.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = note.studentName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Grateful Student",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box {
                Icon(
                    Icons.Default.FormatQuote,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .alpha(0.1f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = note.message,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAppreciationDialog(
    gurus: List<com.example.nimma_guru.data.model.User>,
    onDismiss: () -> Unit, 
    onConfirm: (String, String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    var selectedGuru by remember { mutableStateOf<com.example.nimma_guru.data.model.User?>(null) }
    var expanded by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share your gratitude") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedGuru?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Guru") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        gurus.forEach { guru ->
                            DropdownMenuItem(
                                text = { Text(guru.name) },
                                onClick = {
                                    selectedGuru = guru
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Your Message") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Say something nice...") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (message.isNotBlank() && selectedGuru != null) onConfirm(selectedGuru!!.id, message) },
                enabled = message.isNotBlank() && selectedGuru != null
            ) { Text("Post") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
