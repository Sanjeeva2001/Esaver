package com.example.energysaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalConfiguration
import com.example.energysaver.ui.theme.EnergySaverTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AppBackground = Color(0xFFF3F8EE)
private val PanelBackground = Color(0xFFE6F2D0)
private val CardBackground = Color.White
private val DarkGreen = Color(0xFF5C902D)
private val DeepGreen = Color(0xFF4A821E)
private val TextPrimary = Color(0xFF183D2D)
private val TextSecondary = Color(0xFF5A7663)
private val Border = Color(0xFFDAE7CC)
private val FieldFill = Color(0xFFF7FBF2)
private val ChipFill = Color(0xFFEAF2C7)
private val ChipSelectedFill = Color(0xFF5C902D)
private val ChipSelectedText = Color.White
private val AlertFill = Color(0xFFDDEEB7)
private val WarningText = Color(0xFF6B4A00)

private enum class AppPage(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
) {
    Home("Home", "See today’s energy snapshot", Icons.Rounded.Home),
    Log("Log", "Record your energy use", Icons.Rounded.Add),
    History("History", "View and manage past entries", Icons.Rounded.History),
    Charts("Charts", "Compare trends and categories", Icons.Rounded.BarChart),
    Tips("Tips", "Save energy with small actions", Icons.Rounded.Lightbulb),
    Profile("Profile", "Create your account", Icons.Rounded.Person)
}

private data class ActivityEntry(
    val title: String,
    val subtitle: String,
    val kwh: String,
    val co2: String,
    val icon: ImageVector = Icons.Rounded.Bolt
)

private data class TipEntry(
    val title: String,
    val description: String,
    val badge: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var screen by remember { mutableStateOf("login") }

            when (screen) {

                "login" -> LogInCompose(
                    onLoginClick = { screen = "first page" },
                    onForgotPasswordClick = { screen = "forgot password" },
                    onSignUpClick = { screen = "signup" }
                )

                "forgot password" -> ForgotPasswordComposable(
                    onBack = { screen = "login" }
                )

                "signup" -> ProfileScreen()

                "first page" -> EnergySaverApp()
            }
//


        }
    }
}

@Composable
private fun EnergySaverApp() {
    var currentPage by rememberSaveable { mutableStateOf(AppPage.Home) }
    Scaffold(containerColor = AppBackground) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(innerPadding)
        ) {
            AppSidebar(
                currentPage = currentPage,
                onPageSelected = { currentPage = it }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(AppBackground)
            ) {
                ScreenContent(
                    currentPage = currentPage,
                    onLogClick = { currentPage = AppPage.Log }
                )
            }
        }
    }
}

@Composable
private fun ScreenContent(
    currentPage: AppPage,
    onLogClick: () -> Unit
) {
    when (currentPage) {
        AppPage.Home -> HomeScreen(onLogClick = onLogClick)
        AppPage.Log -> LogEnergyScreen()
        AppPage.History -> HistoryScreen()
        AppPage.Charts -> ChartsScreen()
        AppPage.Tips -> TipsScreen()
        AppPage.Profile -> ProfileScreen()
    }
}

@Composable
private fun AppSidebar(
    currentPage: AppPage,
    onPageSelected: (AppPage) -> Unit
) {
    val compact = LocalConfiguration.current.screenWidthDp < 600
    NavigationRail(
        modifier = Modifier
            .width(if (compact) 72.dp else 80.dp)
            .fillMaxHeight()
            .background(Color(0xFFEAF3DF))
            .padding(vertical = 14.dp),
        containerColor = Color(0xFFEAF3DF),
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu",
                        tint = TextPrimary
                    )
                }

                Surface(
                    modifier = Modifier.size(if (compact) 42.dp else 46.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFD5EAB1)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Bolt,
                            contentDescription = null,
                            tint = DarkGreen
                        )
                    }
                }
            }
        }
    ) {
        AppPage.entries.filter { it != AppPage.Profile }.forEach { page ->
            NavigationRailItem(
                selected = currentPage == page,
                onClick = { onPageSelected(page) },
                icon = { Icon(page.icon, contentDescription = page.title) },
                label = { Text(page.title, fontSize = if (compact) 10.sp else 11.sp) },
                alwaysShowLabel = true,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = DarkGreen,
                    selectedTextColor = DarkGreen,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color(0xFFD8EAB9)
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationRailItem(
            selected = currentPage == AppPage.Profile,
            onClick = { onPageSelected(AppPage.Profile) },
            icon = { Icon(AppPage.Profile.icon, contentDescription = AppPage.Profile.title) },
            label = { Text(AppPage.Profile.title, fontSize = if (compact) 10.sp else 11.sp) },
            alwaysShowLabel = true,
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = DarkGreen,
                selectedTextColor = DarkGreen,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = Color(0xFFD8EAB9)
            )
        )
    }
}

@Composable
private fun PageHeader(
    title: String,
    subtitle: String,
    withAvatar: Boolean = false
) {
    val compact = LocalConfiguration.current.screenWidthDp < 600
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (compact) 14.dp else 18.dp,
                top = if (compact) 14.dp else 18.dp,
                end = if (compact) 14.dp else 18.dp,
                bottom = if (compact) 6.dp else 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = if (compact) 20.sp else 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = if (compact) 12.sp else 13.sp
            )
        }

        if (withAvatar) {
            Surface(
                modifier = Modifier.size(if (compact) 32.dp else 34.dp),
                shape = CircleShape,
                color = Color(0xFFDCEBC2)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Profile",
                        tint = DeepGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onLogClick: () -> Unit) {
    val today = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault()))
    }
    val recentActivity = remember {
        listOf(
            ActivityEntry("Air Conditioner", "Today, 10:30 AM", "2.1 kWh", "0.8 kg CO₂"),
            ActivityEntry("Washing Machine", "Today, 8:00 AM", "1.4 kWh", "0.5 kg CO₂"),
            ActivityEntry("EV Charging", "Yesterday, 11 PM", "12 kWh", "4.6 kg CO₂")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PageHeader(
            title = "Good morning 🌱",
            subtitle = today,
            withAvatar = true
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 18.dp,
                top = 6.dp,
                end = 18.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { WeeklyGoalCard() }
            item { SummaryStatsRow() }
            item {
                SectionTitle(text = "Recent Activity")
            }
            items(recentActivity) { entry ->
                ActivityCard(entry = entry)
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onLogClick,
                        modifier = Modifier.padding(top = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Energy Use")
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyGoalCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 0.70f },
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 7.dp,
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.18f)
                )
                Text(
                    text = "70%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Weekly CO₂ Goal",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "18.6 / 15 kg this week",
                    color = Color(0xFFF5FFF0),
                    fontSize = 14.sp
                )
                Text(
                    text = "Reduce by 3.6 kg to hit goal",
                    color = Color(0xFFE2F6D4),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SummaryStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Bolt,
            value = "4.2",
            label = "kWh Today"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Eco,
            value = "1.9",
            label = "kg CO₂ Today"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Opacity,
            value = "320",
            label = "Litres H₂O"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = PanelBackground,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(16.dp))
                }
            }
            Text(text = value, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = TextSecondary, fontSize = 10.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ActivityCard(entry: ActivityEntry) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = ChipFill, modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(entry.icon, contentDescription = null, tint = DarkGreen)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = entry.subtitle, color = TextSecondary, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = entry.kwh, color = DarkGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = entry.co2, color = Color(0xFFFF6D2E), fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LogEnergyScreen() {
    val compact = LocalConfiguration.current.screenWidthDp < 600
    var selectedCategory by rememberSaveable { mutableStateOf("Appliances") }
    var selectedBuilding by rememberSaveable { mutableStateOf("Residential") }
    var energyUsed by rememberSaveable { mutableStateOf("") }
    var hoursUsed by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    val categories = remember { listOf("Appliances", "Heating", "Lighting", "Smart", "Cooking", "Other") }
    val buildingTypes = remember { listOf("Residential", "Commercial") }
    val dateValue = remember { "24/04/2026" }
    val timeValue = remember { "10:30 AM" }
    val co2Value = energyUsed.toFloatOrNull()?.let { String.format(Locale.getDefault(), "%.2f kg", it * 0.78f) } ?: "—"

    Column(modifier = Modifier.fillMaxSize()) {
        PageHeader(
            title = "Log Energy Use",
            subtitle = "Record your energy consumption to track your footprint"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 18.dp,
                top = 6.dp,
                end = 18.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                FormSection(title = "Category") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categories.forEach { category ->
                            SelectableChip(
                                text = category,
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category }
                            )
                        }
                    }
                }
            }

            item {
                FormSection(title = "Device / Appliance") {
                    ReadOnlyField(
                        value = "Select device...",
                        trailingIcon = Icons.Rounded.ArrowDropDown
                    )
                }
            }

            item {
                FormSection(title = "Building Type") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        buildingTypes.forEach { type ->
                            SelectableChip(
                                text = type,
                                selected = selectedBuilding == type,
                                onClick = { selectedBuilding = type }
                            )
                        }
                    }
                }
            }

            item {
                if (compact) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("Energy Used (kWh)")
                            AppTextField(
                                value = energyUsed,
                                onValueChange = { energyUsed = it },
                                placeholder = "e.g. 1.5"
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("CO₂ (auto-calc)")
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFD8EAB9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = co2Value, color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(text = "Updates automatically", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("Energy Used (kWh)")
                            AppTextField(
                                value = energyUsed,
                                onValueChange = { energyUsed = it },
                                placeholder = "e.g. 1.5"
                            )
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("CO₂ (auto-calc)")
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFD8EAB9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = co2Value, color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(text = "Updates automatically", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel("Smart Appliance Usage (hours)")
                    AppTextField(
                        value = hoursUsed,
                        onValueChange = { hoursUsed = it },
                        placeholder = "e.g. 3.5"
                    )
                    Text(
                        text = "From your IoT sensor dataset (Smart_Appliance_Usage_hours)",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            item {
                if (compact) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("Date")
                            ReadOnlyField(value = dateValue, trailingIcon = Icons.Rounded.CalendarMonth)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("Time")
                            ReadOnlyField(value = timeValue, trailingIcon = Icons.Rounded.AccessTime)
                        }
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("Date")
                            ReadOnlyField(value = dateValue, trailingIcon = Icons.Rounded.CalendarMonth)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel("Time")
                            ReadOnlyField(value = timeValue, trailingIcon = Icons.Rounded.AccessTime)
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel("Notes (optional)")
                    AppTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = "Any extra details...",
                        minLines = 4
                    )
                }
            }

            item {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Save Entry")
                }
            }
        }
    }
}

@Composable
private fun HistoryScreen() {
    val entries = remember {
        listOf(
            ActivityEntry("Air Conditioner", "Appliances · Apr 16 10:30", "2.1 kWh", "1.66 kg CO₂"),
            ActivityEntry("Washing Machine", "Appliances · Apr 16 08:00", "1.4 kWh", "1.11 kg CO₂"),
            ActivityEntry("Smart Appliances", "Smart · Apr 15 14:00", "4.8 kWh", "3.79 kg CO₂"),
            ActivityEntry("LED Lights", "Lighting · Apr 15 19:00", "0.3 kWh", "0.24 kg CO₂"),
            ActivityEntry("Electric Heater", "Heating · Apr 14 07:00", "3.5 kWh", "2.77 kg CO₂"),
            ActivityEntry("Induction Cooktop", "Cooking · Apr 14 18:30", "0.9 kWh", "0.71 kg CO₂"),
            ActivityEntry("Other Device", "Other · Apr 13 21:00", "1.2 kWh", "0.95 kg CO₂")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PageHeader(
            title = "Energy History",
            subtitle = "Tap an entry to view, edit or delete"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 18.dp,
                top = 6.dp,
                end = 18.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    SmallSummaryCard(modifier = Modifier.weight(1f), icon = Icons.Rounded.Bolt, value = "14.2", label = "Total kWh")
                    SmallSummaryCard(modifier = Modifier.weight(1f), icon = Icons.Rounded.Eco, value = "11.23", label = "Total CO₂")
                    SmallSummaryCard(modifier = Modifier.weight(1f), icon = Icons.Rounded.History, value = "7", label = "Entries")
                }
            }
            items(entries) { entry ->
                HistoryEntryCard(entry)
            }
        }
    }
}

@Composable
private fun SmallSummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(shape = CircleShape, color = PanelBackground, modifier = Modifier.size(26.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(15.dp))
                }
            }
            Text(text = value, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
private fun HistoryEntryCard(entry: ActivityEntry) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = ChipFill, modifier = Modifier.size(38.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Bolt, contentDescription = null, tint = DarkGreen)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = entry.subtitle, color = TextSecondary, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = entry.kwh, color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = entry.co2, color = Color(0xFFFF6D2E), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ChartsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        PageHeader(
            title = "Charts",
            subtitle = "Track usage and compare category breakdowns"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 18.dp,
                top = 6.dp,
                end = 18.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { ChartCard() }
            item { BreakdownCard() }
            item { GoalCard(title = "Goal progress", progress = "18.6 / 15 kg this week", footer = "You are 70% toward the weekly goal") }
        }
    }
}

@Composable
private fun ChartCard() {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Weekly usage", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(text = "Bar chart placeholder using local UI only", color = TextSecondary, fontSize = 12.sp)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF6FBF2))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                val values = listOf(0.25f, 0.42f, 0.68f, 0.59f, 0.74f, 0.88f, 0.55f)
                val barWidth = size.width / (values.size * 1.7f)
                val gap = barWidth * 0.7f
                val maxHeight = size.height * 0.8f
                values.forEachIndexed { index, value ->
                    val left = index * (barWidth + gap) + gap
                    val barHeight = maxHeight * value
                    drawRoundRect(
                        color = DarkGreen,
                        topLeft = Offset(left, size.height - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LegendPill(Color(0xFF5C902D), "Usage")
                LegendPill(Color(0xFFCDE3A2), "Target")
            }
        }
    }
}

@Composable
private fun BreakdownCard() {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = "Category breakdown", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            BreakdownRow("Appliances", 0.46f, "46%")
            BreakdownRow("Heating", 0.26f, "26%")
            BreakdownRow("Lighting", 0.14f, "14%")
            BreakdownRow("Other", 0.14f, "14%")
        }
    }
}

@Composable
private fun BreakdownRow(label: String, fraction: Float, percent: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, color = TextPrimary, fontSize = 13.sp)
            Text(text = percent, color = TextSecondary, fontSize = 12.sp)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0xFFDCEBDD))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(9.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(DarkGreen)
            )
        }
    }
}

@Composable
private fun TipsScreen() {
    val tips = remember {
        listOf(
            TipEntry("Run heavy appliances off-peak", "Delay laundry and dishwashing until lower-demand hours.", "Best impact"),
            TipEntry("Use natural light", "Open curtains during the day before switching lights on.", "Easy win"),
            TipEntry("Reduce standby power", "Switch off devices at the wall when they are not needed.", "Low effort"),
            TipEntry("Keep thermostat steady", "A stable temperature usually costs less than big swings.", "Comfortable")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PageHeader(
            title = "Energy Tips",
            subtitle = "Small changes that add up over time"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 18.dp,
                top = 6.dp,
                end = 18.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF5C902D))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "This week’s tip goal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(text = "Apply 3 energy-saving actions to unlock your progress badge.", color = Color(0xFFEAF8D9), fontSize = 13.sp)
                    }
                }
            }
            items(tips) { tip ->
                TipCard(tip)
            }
        }
    }
}

@Composable
private fun TipCard(tip: TipEntry) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = PanelBackground, modifier = Modifier.size(38.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Lightbulb, contentDescription = null, tint = DarkGreen)
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = tip.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = tip.description, color = TextSecondary, fontSize = 12.sp)
            }
            Surface(shape = RoundedCornerShape(999.dp), color = ChipFill) {
                Text(
                    text = tip.badge,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = DarkGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProfileScreen() {
    var fullName by rememberSaveable { mutableStateOf("Jane Smith") }
    var email by rememberSaveable { mutableStateOf("jane@email.com") }
    var age by rememberSaveable { mutableStateOf("25") }
    var city by rememberSaveable { mutableStateOf("Melbourne, VIC") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var agreeTerms by rememberSaveable { mutableStateOf(false) }
    var consentInsights by rememberSaveable { mutableStateOf(false) }
    var consentEmails by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        PageHeader(
            title = "Create Account",
            subtitle = "Join E-Saver and start tracking your carbon footprint"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 18.dp,
                top = 6.dp,
                end = 18.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                FormSection(title = "Personal Info") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SectionLabel("Full Name")
                            AppTextField(value = fullName, onValueChange = { fullName = it }, placeholder = "Jane Smith")
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SectionLabel("Email Address")
                            AppTextField(value = email, onValueChange = { email = it }, placeholder = "jane@email.com")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(0.8f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                SectionLabel("Age")
                                AppTextField(value = age, onValueChange = { age = it }, placeholder = "25")
                            }
                            Column(modifier = Modifier.weight(1.3f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                SectionLabel("City / Region")
                                AppTextField(value = city, onValueChange = { city = it }, placeholder = "Melbourne, VIC")
                            }
                        }
                    }
                }
            }

            item {
                FormSection(title = "Password") {
                    Surface(shape = RoundedCornerShape(12.dp), color = AlertFill) {
                        Text(
                            text = "Guidelines: ⓘ Min. 8 characters • Mix of uppercase, lowercase, number & special char (e.g. !@#)",
                            modifier = Modifier.padding(12.dp),
                            color = WarningText,
                            fontSize = 12.sp
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SectionLabel("Password")
                            AppTextField(value = password, onValueChange = { password = it }, placeholder = "Min. 8 characters", isPassword = true)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SectionLabel("Confirm Password")
                            AppTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Repeat password", isPassword = true)
                        }
                    }
                }
            }

            item {
                FormSection(title = "Agreements") {
                    CheckboxRow(
                        checked = agreeTerms,
                        onCheckedChange = { agreeTerms = it },
                        text = "I agree to the Terms of Service and Privacy Policy *"
                    )
                    CheckboxRow(
                        checked = consentInsights,
                        onCheckedChange = { consentInsights = it },
                        text = "I consent to my energy data being used to generate personalised insights *"
                    )
                    CheckboxRow(
                        checked = consentEmails,
                        onCheckedChange = { consentEmails = it },
                        text = "Send me weekly energy saving tips via email"
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Create Account")
                }
            }
        }
    }
}

@Composable
private fun CheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String
) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = text, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun ChartsLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun LegendPill(color: Color, label: String) {
    Surface(shape = RoundedCornerShape(999.dp), color = Color.Transparent, border = BorderStroke(1.dp, Border)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
            Text(text = label, color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun GoalCard(
    title: String,
    progress: String,
    footer: String
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = progress, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFDCEBDD))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.70f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(DarkGreen)
                )
            }
            Text(text = footer, color = TextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = if (selected) ChipSelectedFill else ChipFill,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (selected) ChipSelectedText else TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ReadOnlyField(
    value: String,
    trailingIcon: ImageVector
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = FieldFill,
        border = BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = value, color = TextSecondary, modifier = Modifier.weight(1f), fontSize = 14.sp)
            Icon(trailingIcon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = minLines == 1,
        minLines = minLines,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = FieldFill,
            unfocusedContainerColor = FieldFill,
            disabledContainerColor = FieldFill,
            focusedBorderColor = Border,
            unfocusedBorderColor = Border,
            focusedPlaceholderColor = TextSecondary,
            unfocusedPlaceholderColor = TextSecondary,
            cursorColor = DarkGreen
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EnergySaverPreview() {
    EnergySaverTheme {
        EnergySaverApp()
    }
}
