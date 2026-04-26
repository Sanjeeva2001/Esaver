package com.example.esaver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class EnergyEntry(
    val id: Int,
    val name: String,
    val category: String,
    val date: String,
    val kwh: String,
    val co2: String,
    val buildingType: String,
    val co2Factor: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    var search by remember { mutableStateOf("") }

    val entries = listOf(
        EnergyEntry(1, "Air Conditioner", "Appliances", "Apr 16 10:30", "2.1 kWh", "1.66 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)"),
        EnergyEntry(2, "Washing Machine", "Appliances", "Apr 16 08:00", "1.4 kWh", "1.11 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)"),
        EnergyEntry(3, "Smart Appliances", "Smart", "Apr 15 14:00", "4.8 kWh", "3.79 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)"),
        EnergyEntry(4, "LED Lights", "Lighting", "Apr 15 19:00", "0.3 kWh", "0.24 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)"),
        EnergyEntry(5, "Electric Heater", "Heating", "Apr 14 07:00", "3.5 kWh", "2.77 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)"),
        EnergyEntry(6, "Induction Cooktop", "Cooking", "Apr 14 18:30", "0.9 kWh", "0.71 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)"),
        EnergyEntry(7, "Other Device", "Other", "Apr 13 21:00", "1.2 kWh", "0.95 kg CO₂", "Residential", "0.79 kg/kWh (Aus grid)")
    )

    var expandedId by remember { mutableStateOf<Int?>(null) }

    val backgroundColor = Color(0xFFF1F8E9)
    val primaryGreen = Color(0xFF2E7D32)
    val paleGreen = Color(0xFFE8F5E9)
    val co2Orange = Color(0xFFE65100)
    val deleteBg = Color(0xFFFFEBEE)
    val deleteText = Color(0xFFD32F2F)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Energy History",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tap an entry to view, edit or delete",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp,
                vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.ElectricBolt, contentDescription = "Total kWh", tint = primaryGreen) },
                value = "14.2",
                label = "Total kWh"
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.Eco, contentDescription = "Total CO2", tint = primaryGreen) },
                value = "11.23 kg",
                label = "Total CO₂"
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.Menu, contentDescription = "Entries", tint = primaryGreen) },
                value = "7",
                label = "Entries"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        entries.forEach { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expandedId = if (expandedId == entry.id) null else entry.id },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(paleGreen, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = entry.name,
                                tint = primaryGreen
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entry.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${entry.category} · ${entry.date}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = entry.kwh,
                                style = MaterialTheme.typography.bodyMedium,
                                color = primaryGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = entry.co2,
                                style = MaterialTheme.typography.bodySmall,
                                color = co2Orange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    AnimatedVisibility(visible = expandedId == entry.id) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Building Type",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = entry.buildingType,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "CO₂ Factor",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = entry.co2Factor,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Date/Time",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = entry.date,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {},
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = paleGreen,
                                        contentColor = primaryGreen
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Edit")
                                }

                                Button(
                                    onClick = {},
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = deleteBg,
                                        contentColor = deleteText
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}
