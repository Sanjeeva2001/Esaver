package com.example.energysaver

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
        var username = remember { mutableStateOf("") }
        var password = remember { mutableStateOf("") }
        Column(modifier = Modifier.padding(horizontal = 60.dp, vertical = 300.dp)) {
            if (username.value.isNotEmpty()) {
                Text(
                    text = "Hello, ${username.value}!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") }
            )

        }
        Column(modifier = Modifier.padding(horizontal = 60.dp, vertical = 360.dp)) {
            if (password.value.isNotEmpty()) {
                Text(
                    text = "Hello, ${password.value}!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") }
            )

        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            ),
            modifier = Modifier.padding(horizontal = 100.dp, vertical = 400.dp)
        ) {
            Text("Login")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LogInCompose()
}