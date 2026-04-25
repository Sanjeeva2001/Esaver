package com.example.esaver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Greeting row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Hi Welcome 🌱",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Weekly CO2 Goal card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.7f },
                        modifier = Modifier.size(64.dp),
                        color = Color.White,
                        strokeWidth = 6.dp,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Text(
                        "70%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        "Weekly CO₂ Goal",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "18.6 / 15 kg this week",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Reduce by 3.6 kg to hit goal",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 3 stat cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(Modifier.weight(1f), "4.2", "kWh Today")
            StatCard(Modifier.weight(1f), "1.9", "kg CO₂")
            StatCard(Modifier.weight(1f), "320", "Litres H₂O")
        }

        Spacer(Modifier.height(16.dp))

        Text("Recent Activity", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        // Activity list
        val activities = listOf(
            Triple("Air Conditioner", "Today, 10:30 AM", "0.8 kg CO₂"),
            Triple("Washing Machine", "Today, 8:00 AM", "0.5 kg CO₂"),
            Triple("EV Charging", "Yesterday, 11 PM", "4.6 kg CO₂")
        )

        activities.forEach { (name, time, co2) ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Text(
                        co2,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Log Energy button
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Log Energy Use", color = Color.White)
            }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, value: String, label: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Eco, contentDescription = null, tint = Color(0xFF2E7D32))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1)
        }
    }
}
