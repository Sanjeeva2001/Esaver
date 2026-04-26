package com.example.esaver

import androidx.compose.foundation.background
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

private val PrimaryGreen = Color(0xFF2E7D32)
private val PaleGreen = Color(0xFFE8F5E9)
private val BgGreen = Color(0xFFF1F8E9)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LogScreen() {
    var selectedCategory by remember { mutableStateOf("Appliances") }
    var selectedDevice by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedBuilding by remember { mutableStateOf("Residential") }
    var energyUsed by remember { mutableStateOf("") }
    var smartUsage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val co2Value = energyUsed.toFloatOrNull()?.let { String.format(Locale.US, "%.2f", it * 0.233f) } ?: ""

    val categories = listOf("Appliances", "Heating", "Lighting", "Smart", "Cooking", "Other")
    val devices = listOf("Air Conditioner", "Washing Machine", "Dryer", "Refrigerator", "Dishwasher")
    val buildingTypes = listOf("Residential", "Commercial")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGreen)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp)
    ) {
        // 1. Header
        Text(
            "Log Energy Use",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Record your energy consumption to track your footprint",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(Modifier.height(12.dp))

        // 2. Category
        Text(
            text = buildAnnotatedString {
                append("Category ")
                withStyle(SpanStyle(color = Color.Red)) {
                    append("*")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = spacedBy(8.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val selected = selectedCategory == category
                FilterChip(
                    selected = selected,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = PaleGreen,
                        labelColor = PrimaryGreen,
                        selectedContainerColor = PrimaryGreen,
                        selectedLabelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = PaleGreen,
                        selectedBorderColor = PrimaryGreen
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 3. Device / Appliance
        Text(
            text = buildAnnotatedString {
                append("Device / Appliance ")
                withStyle(SpanStyle(color = Color.Red)) {
                    append("*")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedDevice,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select device...") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                devices.forEach { device ->
                    DropdownMenuItem(
                        text = { Text(device) },
                        onClick = {
                            selectedDevice = device
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(textColor = Color.Black)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 4. Building Type
        Text("Building Type", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = spacedBy(8.dp)) {
            buildingTypes.forEach { type ->
                val selected = selectedBuilding == type
                FilterChip(
                    selected = selected,
                    onClick = { selectedBuilding = type },
                    label = { Text(type) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = PaleGreen,
                        labelColor = PrimaryGreen,
                        selectedContainerColor = PrimaryGreen,
                        selectedLabelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = PaleGreen,
                        selectedBorderColor = PrimaryGreen
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 5. Energy Used + CO2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        append("Energy Used (kWh) ")
                        withStyle(SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = energyUsed,
                    onValueChange = { energyUsed = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. 1.5") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "CO₂ (auto-calc)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = co2Value,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("—") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = PaleGreen,
                        disabledContainerColor = PaleGreen
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 6. Smart Appliance Usage
        Text(
            "Smart Appliance Usage (hours)",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = smartUsage,
            onValueChange = { smartUsage = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. 3.5") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(Modifier.height(12.dp))

        // 7. Date + Time
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        append("Date ")
                        withStyle(SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = "24/04/2026",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Date") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Time", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = "10:30 AM",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = "Time") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 8. Notes
        Text("Notes (optional)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Any extra details...") },
            minLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        // 9. Save Button
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Save Entry", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
