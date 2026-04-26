// Author: madhumithachalla
// ChartsScreen - Usage & Analytics dashboard
// Displays daily energy bar chart, CO2 donut chart, and weekly summary
// Part of ESaver - SDG 7 Carbon Footprint Tracker

package com.example.esaver

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GreenDark = Color(0xFF2E7D32)
private val GreenLight = Color(0xFF81C784)
private val GreenBg = Color(0xFFF1F8E9)
private val GreenCard = Color(0xFFE8F5E9)

@Composable
fun ChartsScreen() {
    val tabs = listOf("Week", "Month", "Year")
    var selectedTab by remember { mutableStateOf(0) }

    // Weekly bar data
    val weeklyData = listOf(
        "Mon" to 3.2f,
        "Tue" to 4.1f,
        "Wed" to 2.8f,
        "Thu" to 5.0f,
        "Fri" to 4.2f,
        "Sat" to 3.7f,
        "Sun" to 4.2f
    )
    val maxValue = weeklyData.maxOf { it.second }

    // CO2 donut data
    val co2Categories = listOf(
        Triple("Appliances", 0.35f, Color(0xFF2E7D32)),
        Triple("Transport",  0.25f, Color(0xFFFF8F00)),
        Triple("Heating",    0.20f, Color(0xFF81C784)),
        Triple("Lighting",   0.12f, Color(0xFF26C6DA)),
        Triple("Cooking",    0.08f, Color(0xFFFFCC02))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Usage & Analytics",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Week / Month / Year tabs
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            tabs.forEachIndexed { index, label ->
                val selected = selectedTab == index
                Surface(
                    onClick = { selectedTab = index },
                    shape = RoundedCornerShape(50),
                    color = if (selected) GreenDark else GreenCard,
                    modifier = Modifier.height(36.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (selected) Color.White else Color.Black,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // ── Daily Energy Bar Chart ──────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Daily Energy (kWh)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyData.forEach { (day, value) ->
                        val isMax = value == maxValue
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = value.toString(),
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            val barHeightFraction = value / maxValue
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight(barHeightFraction)
                                    .background(
                                        color = if (isMax) GreenDark else GreenLight,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = day,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── CO2 by Category Donut Chart ─────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "CO₂ by Category (kg)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Donut
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(110.dp)
                    ) {
                        Canvas(modifier = Modifier.size(110.dp)) {
                            var startAngle = -90f
                            val strokeWidth = 22f
                            co2Categories.forEach { (_, fraction, color) ->
                                val sweep = fraction * 360f
                                drawArc(
                                    color = color,
                                    startAngle = startAngle,
                                    sweepAngle = sweep,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth),
                                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                                    size = Size(
                                        size.width - strokeWidth,
                                        size.height - strokeWidth
                                    )
                                )
                                startAngle += sweep
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "18.6 kg",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    // Legend
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        co2Categories.forEach { (name, fraction, color) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(color, CircleShape)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(name, fontSize = 12.sp)
                                }
                                Text(
                                    "${(fraction * 100).toInt()}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Weekly Summary ──────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Weekly Summary",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        value = "27.2",
                        label = "Total kWh",
                        subLabel = "↓8%"
                    )
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        value = "10.3 kg",
                        label = "Total CO₂",
                        subLabel = "↓5%"
                    )
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        value = "69%",
                        label = "vs Goal",
                        subLabel = "On track",
                        subLabelColor = GreenDark
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    subLabel: String,
    subLabelColor: Color = Color.Gray
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GreenCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
            Text(subLabel, fontSize = 10.sp, color = subLabelColor, fontWeight = FontWeight.Medium)
        }
    }
}