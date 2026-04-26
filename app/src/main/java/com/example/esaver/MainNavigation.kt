package com.example.esaver

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    var railOpen by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ESaver",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { railOpen = !railOpen }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (railOpen) {
                NavigationRail(
                    containerColor = Color(0xFF2E7D32)
                ) {
                    Destination.entries.forEach { dest ->
                        NavigationRailItem(
                            selected = currentRoute == dest.route,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, contentDescription = dest.label) },
                            label = { Text(dest.label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.85f),
                                unselectedTextColor = Color.White.copy(alpha = 0.85f),
                                indicatorColor = Color.White.copy(alpha = 0.18f)
                            )
                        )
                    }
                }
            }
            NavHost(
                navController = navController,
                startDestination = Destination.HOME.route,
                modifier = Modifier.weight(1f)
            ) {
                composable(Destination.HOME.route) { HomeScreen() }
                composable(Destination.CHARTS.route) { ChartsScreen() }
                composable(Destination.LOG.route) { LogScreen() }
                composable(Destination.HISTORY.route) { HistoryScreen() }
                composable(Destination.LOGIN.route) {
                    LoginScreen(
                        onLoginClick = {
                            navController.navigate(Destination.HOME.route)
                        },
                        onForgotPasswordClick = {
                            navController.navigate("forgot")
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                }
                composable("forgot") {
                    ForgotPasswordComposable(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("register") { RegisterScreen() }
                composable(Destination.PROFILE.route) { ProfileScreen() }
            }
        }
    }
}
