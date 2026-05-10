package com.example.nimma_guru.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nimma_guru.R
import com.example.nimma_guru.data.model.UserRole
import com.example.nimma_guru.ui.screens.AuthScreen
import com.example.nimma_guru.ui.screens.OnboardingScreen
import com.example.nimma_guru.ui.screens.admin.AdminDashboardScreen
import com.example.nimma_guru.ui.screens.guru.GuruDashboardScreen
import com.example.nimma_guru.ui.screens.guru.GuruProfileScreen
import com.example.nimma_guru.ui.screens.student.*
import com.example.nimma_guru.ui.viewmodel.AuthState
import com.example.nimma_guru.ui.viewmodel.AuthViewModel
import com.example.nimma_guru.ui.viewmodel.GuruViewModel
import kotlinx.serialization.Serializable

@Serializable object StudentHome
@Serializable object StudentSearch
@Serializable object StudentMap
@Serializable object StudentCalendar
@Serializable object StudentWallOfFame
@Serializable object GuruDashboard
@Serializable object GuruProfile
@Serializable object AdminDashboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NimmaGuruApp() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val guruViewModel: GuruViewModel = hiltViewModel()
    
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val userRole by authViewModel.currentUserRole.collectAsState()
    
    var showOnboarding by remember { mutableStateOf(true) }

    if (showOnboarding) {
        OnboardingScreen(onFinish = { showOnboarding = false })
    } else if (authState !is AuthState.Authenticated) {
        AuthScreen(authViewModel)
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    actions = {
                        IconButton(onClick = { authViewModel.signOut() }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                        }
                    }
                )
            },
            bottomBar = {
                when (userRole) {
                    UserRole.STUDENT -> StudentBottomBar(navController)
                    UserRole.GURU -> GuruBottomBar(navController)
                    UserRole.ADMIN -> AdminBottomBar(navController)
                    else -> {}
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = when (userRole) {
                    UserRole.STUDENT -> StudentHome
                    UserRole.GURU -> GuruDashboard
                    UserRole.ADMIN -> AdminDashboard
                    else -> StudentHome
                },
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<StudentHome> { StudentHomeScreen(guruViewModel) }
                composable<StudentSearch> { StudentSearchScreen(guruViewModel, onGuruClick = {}) }
                composable<StudentMap> { StudentMapScreen(guruViewModel) }
                composable<StudentCalendar> { StudentCalendarScreen() }
                composable<StudentWallOfFame> { StudentWallOfFameScreen() }
                composable<GuruDashboard> { GuruDashboardScreen() }
                composable<GuruProfile> { GuruProfileScreen(guruViewModel) }
                composable<AdminDashboard> { AdminDashboardScreen() }
            }
        }
    }
}

@Composable
fun StudentBottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = currentDestination?.route?.contains("StudentHome") == true,
            onClick = { navController.navigate(StudentHome) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("Search") },
            selected = currentDestination?.route?.contains("StudentSearch") == true,
            onClick = { navController.navigate(StudentSearch) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Map, contentDescription = null) },
            label = { Text("Map") },
            selected = currentDestination?.route?.contains("StudentMap") == true,
            onClick = { navController.navigate(StudentMap) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) },
            label = { Text("Wall") },
            selected = currentDestination?.route?.contains("StudentWallOfFame") == true,
            onClick = { navController.navigate(StudentWallOfFame) }
        )
    }
}

@Composable
fun GuruBottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Dashboard") },
            selected = currentDestination?.route?.contains("GuruDashboard") == true,
            onClick = { navController.navigate(GuruDashboard) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = currentDestination?.route?.contains("GuruProfile") == true,
            onClick = { navController.navigate(GuruProfile) }
        )
    }
}

@Composable
fun AdminBottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Verify") },
            selected = currentDestination?.route?.contains("AdminDashboard") == true,
            onClick = { navController.navigate(AdminDashboard) }
        )
    }
}
