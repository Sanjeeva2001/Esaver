package com.example.esaver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GreenDark   = Color(0xFF2E7D32)
private val GreenLight  = Color(0xFF81C784)
private val GreenBg     = Color(0xFFF1F8E9)
private val GreenCard   = Color(0xFFE8F5E9)

@Composable
fun ProfileScreen() {
    var dailyReminder  by remember { mutableStateOf(true) }
    var weeklyReport   by remember { mutableStateOf(true) }
    var co2GoalKg      by remember { mutableFloatStateOf(15f) }
    var energyUnitKwh  by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // User Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    // Avatar circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(56.dp)
                            .background(GreenDark, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Jane Smith",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "jane@example.com · Melbourne, VIC",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileStatChip(modifier = Modifier.weight(1f), value = "26",        label = "Age")
                    ProfileStatChip(modifier = Modifier.weight(1f), value = "Apr 2026",  label = "Member Since")
                    ProfileStatChip(modifier = Modifier.weight(1f), value = "47",        label = "Entries")
                }
            }
        }

        //  Notifications Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Notifications",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                NotificationToggleRow(
                    title = "Daily Energy Reminders",
                    subtitle = "AlarmManager scheduled alerts",
                    checked = dailyReminder,
                    onCheckedChange = { dailyReminder = it }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = GreenBg
                )
                NotificationToggleRow(
                    title = "Weekly Report",
                    subtitle = "Summary every Sunday morning",
                    checked = weeklyReport,
                    onCheckedChange = { weeklyReport = it }
                )
            }
        }

        // Weekly CO2 Goal Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Weekly CO₂ Goal",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        "${co2GoalKg.toInt()} kg",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = GreenDark
                    )
                }
                Spacer(Modifier.height(8.dp))
                Slider(
                    value = co2GoalKg,
                    onValueChange = { co2GoalKg = it },
                    valueRange = 5f..30f,
                    colors = SliderDefaults.colors(
                        thumbColor = GreenDark,
                        activeTrackColor = GreenDark,
                        inactiveTrackColor = GreenLight.copy(alpha = 0.4f)
                    )
                )
                Text(
                    "UN SDG 7 recommendation: ≤15 kg CO₂/week",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }

        //  Preferences Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Preferences",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("Energy Unit", fontSize = 13.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    UnitToggleChip(
                        label = "kWh",
                        selected = energyUnitKwh,
                        onClick = { energyUnitKwh = true }
                    )
                    UnitToggleChip(
                        label = "MJ",
                        selected = !energyUnitKwh,
                        onClick = { energyUnitKwh = false }
                    )
                }
            }
        }

        //  Sign Out Button
        OutlinedButton(
            onClick = { /* TODO: sign out */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Red)
        ) {
            Text("Sign Out", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(Modifier.height(8.dp))
    }
}

// Reusable sub-composables

@Composable
private fun ProfileStatChip(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = GreenCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(subtitle, fontSize = 11.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GreenDark,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
private fun UnitToggleChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selected) GreenDark else GreenCard,
        modifier = Modifier.height(36.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                label,
                color = if (selected) Color.White else Color.Black,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}