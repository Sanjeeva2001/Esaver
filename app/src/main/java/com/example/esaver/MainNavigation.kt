package com.example.esaver

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    var expanded by remember { mutableStateOf(true) }

    Row(Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier.width(if (expanded) 120.dp else 80.dp),
            containerColor = MaterialTheme.colorScheme.background,
            header = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.Menu, "Toggle menu")
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
                    icon = { Icon(dest.icon, dest.label) },
                    label = if (expanded) {
                        { Text(dest.label) }
                    } else null,
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Black
                    )
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


