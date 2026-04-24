package com.example.esaver

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Row(Modifier.fillMaxSize()) {
        NavigationRail(
            containerColor = Color(0xFFF1F8E9),
            header = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                Spacer(Modifier.height(8.dp))
            }
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
                    label = { Text(dest.label, style = MaterialTheme.typography.labelSmall) }
                )
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
            composable(Destination.LOGIN.route) { LoginScreen() }
            composable(Destination.PROFILE.route) { ProfileScreen() }
        }
    }
}


