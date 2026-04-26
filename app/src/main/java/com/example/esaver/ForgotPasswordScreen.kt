package com.example.esaver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordComposable(onBack: () -> Unit) {

    val green = Color(0xFF1B5E20)

    var option by remember { mutableStateOf("Email") }
    var input by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE8F5E9)){

        Column(modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Row(modifier = Modifier.fillMaxWidth()) {
                ElevatedButton(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = green
                    )
                ) {
                    Text(text = "Back")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = input,
                onValueChange = {input = it},
                label = {
                    Text(
                        if (option == "Email") "Enter email"
                        else "Enter phone number"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = option == "Email",
                    onClick = { option = "Email" })
                Text(text = "Email")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = option == "SMS",
                    onClick = { option = "SMS" })
                Text(text = "SMS")
            }
            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(onClick = {},
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green,
                    contentColor = Color.White

                )
            ) {
                Text(text = "Send code",
                    fontSize = 16.sp)
            }




        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreens() {


}