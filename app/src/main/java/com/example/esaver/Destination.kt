package com.example.esaver

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val route: String, val icon: ImageVector, val label: String) {
    HOME("home", Icons.Default.Home, "Home"),
    CHARTS("charts", Icons.Default.Info, "Charts"),
    LOG("log", Icons.Default.Add, "Logs"),
    HISTORY("history", Icons.Default.List, "History"),
    LOGIN("login", Icons.Default.Lock, "Login"),
    PROFILE("profile", Icons.Default.Person, "Profile")
}

