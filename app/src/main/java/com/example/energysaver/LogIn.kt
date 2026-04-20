package com.example.energysaver

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

class LogIn {
}

@Composable
fun LogInCompose(modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFE8F5E9)){
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column(modifier = Modifier.padding(horizontal = 60.dp, vertical = 300.dp)) {
            if (username.isNotEmpty()) {
                Text(
                    text = "Hello, $username!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )

        }
        Column(modifier = Modifier.padding(horizontal = 60.dp, vertical = 360.dp)) {
            if (password.isNotEmpty()) {
                Text(
                    text = "Hello, $password!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LogInCompose()
}