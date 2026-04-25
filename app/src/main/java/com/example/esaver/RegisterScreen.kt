package com.example.esaver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsChecked by remember { mutableStateOf(false) }
    var dataConsentChecked by remember { mutableStateOf(false) }
    var tipsChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F8E9))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Personal Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Personal Info", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                Text("Full Name", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Jane Smith") }
                )

                Spacer(Modifier.height(12.dp))

                Text("Email Address", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("jane@email.com") }
                )

                Spacer(Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Age", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("25") }
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(2f)) {
                        Text("City / Region", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Melbourne, VIC") }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Password Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Password", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDDECC1), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Guidelines: Min. 8 characters • Mix of uppercase, lowercase, number & special char (e.g. !@#)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF355E1E)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text("Password", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Min. 8 characters") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(Modifier.height(12.dp))

                Text("Confirm Password", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Repeat password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Agreements Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Agreements", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = termsChecked, onCheckedChange = { termsChecked = it })
                    Text(
                        text = "I agree to the Terms of Service and Privacy Policy *",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = dataConsentChecked, onCheckedChange = { dataConsentChecked = it })
                    Text(
                        text = "I consent to my energy data being used to generate personalised insights *",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = tipsChecked, onCheckedChange = { tipsChecked = it })
                    Text(
                        text = "Send me weekly energy saving tips via email",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("Create Account", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))
    }
}