package com.example.nimma_guru.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.example.nimma_guru.ui.screens.admin.AdminProfileScreen
import com.example.nimma_guru.ui.screens.guru.GuruDashboardScreen
import com.example.nimma_guru.ui.screens.guru.GuruProfileScreen
import com.example.nimma_guru.ui.screens.student.*
import com.example.nimma_guru.ui.viewmodel.AuthState
import com.example.nimma_guru.ui.viewmodel.AuthViewModel
import com.example.nimma_guru.ui.viewmodel.GuruViewModel
import com.example.nimma_guru.ui.viewmodel.SettingsViewModel
import kotlinx.serialization.Serializable

@Serializable object StudentHome
@Serializable object StudentSearch
@Serializable object StudentCalendar
@Serializable object StudentWallOfFame
@Serializable object StudentProfile
@Serializable object GuruDashboard
@Serializable object GuruCalendar
@Serializable object GuruWallOfFame
@Serializable object GuruProfile
@Serializable object AdminDashboard
@Serializable object AdminProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NimmaGuruApp(settingsViewModel: SettingsViewModel) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val guruViewModel: GuruViewModel = hiltViewModel()
    
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val userRole by authViewModel.currentUserRole.collectAsState()
    
    var showOnboarding by remember { mutableStateOf(true) }

    AnimatedContent(
        targetState = showOnboarding to authState,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "AppContentTransition"
    ) { (onboarding, auth) ->
        if (onboarding) {
            OnboardingScreen(onFinish = { showOnboarding = false })
        } else if (auth !is AuthState.Authenticated) {
            AuthScreen(authViewModel)
        } else {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { 
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.chatgpt_image_may_12__2026__04_20_02_pm),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleLarge
                                ) 
                            }
                        },
                        actions = {
                            IconButton(onClick = { authViewModel.signOut() }) {
                                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.primary
                        )
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
                    modifier = Modifier.padding(innerPadding),
                    enterTransition = { slideInHorizontally { it } + fadeIn() },
                    exitTransition = { slideOutHorizontally { -it } + fadeOut() },
                    popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
                    popExitTransition = { slideOutHorizontally { it } + fadeOut() }
                ) {
                    composable<StudentHome> { StudentHomeScreen(guruViewModel) }
                    composable<StudentSearch> { StudentSearchScreen(guruViewModel, onGuruClick = {}) }
                    composable<StudentCalendar> { StudentCalendarScreen() }
                    composable<StudentWallOfFame> { StudentWallOfFameScreen() }
                    composable<StudentProfile> { StudentProfileScreen(guruViewModel, settingsViewModel, authViewModel) }
                    composable<GuruDashboard> { GuruDashboardScreen() }
                    composable<GuruCalendar> { StudentCalendarScreen() }
                    composable<GuruWallOfFame> { StudentWallOfFameScreen() }
                    composable<GuruProfile> { GuruProfileScreen(guruViewModel, settingsViewModel, authViewModel) }
                    composable<AdminDashboard> { AdminDashboardScreen() }
                    composable<AdminProfile> { AdminProfileScreen(settingsViewModel, authViewModel) }
                }
            }
        }
    }
}

@Composable
fun StudentBottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(StudentHome, Icons.Default.Home, "Home"),
            Triple(StudentSearch, Icons.Default.Search, "Search"),
            Triple(StudentCalendar, Icons.Default.CalendarMonth, "Calendar"),
            Triple(StudentWallOfFame, Icons.Default.EmojiEvents, "Wall"),
            Triple(StudentProfile, Icons.Default.Person, "Profile")
        )

        items.forEach { (route, icon, label) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label) },
                selected = currentDestination?.route?.contains(route::class.simpleName ?: "") == true,
                onClick = { 
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun GuruBottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(tonalElevation = 8.dp) {
        val items = listOf(
            Triple(GuruDashboard, Icons.Default.Dashboard, "Dashboard"),
            Triple(GuruCalendar, Icons.Default.CalendarMonth, "Calendar"),
            Triple(GuruWallOfFame, Icons.Default.EmojiEvents, "Wall"),
            Triple(GuruProfile, Icons.Default.Person, "Profile")
        )

        items.forEach { (route, icon, label) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label) },
                selected = currentDestination?.route?.contains(route::class.simpleName ?: "") == true,
                onClick = { 
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AdminBottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
            label = { Text("Verify") },
            selected = currentDestination?.route?.contains("AdminDashboard") == true,
            onClick = { navController.navigate(AdminDashboard) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = currentDestination?.route?.contains("AdminProfile") == true,
            onClick = { navController.navigate(AdminProfile) }
        )
    }
}
