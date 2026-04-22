package com.example.energysaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energysaver.ui.theme.EnergySaverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnergySaverTheme {
                EnergySaverApp()
            }
        }
    }
}

private enum class AppPage(
    val title: String,
    val icon: ImageVector
) {
    Home("Home", Icons.Rounded.Home),
    Insights("Insights", Icons.Rounded.BarChart),
    Profile("Profile", Icons.Rounded.Person)
}

@Composable
private fun EnergySaverApp() {
    var currentPage by rememberSaveable { mutableStateOf(AppPage.Home) }

    Scaffold(
        containerColor = Color(0xFFF2F8F3),
        topBar = { AppHeader() }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F8F3))
                .padding(innerPadding)
        ) {
            AppNavigationRail(
                currentPage = currentPage,
                onPageSelected = { currentPage = it }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                when (currentPage) {
                    AppPage.Home -> HomeDashboardScreen(PaddingValues(0.dp))
                    AppPage.Insights -> InsightDashboardScreen(PaddingValues(0.dp))
                    AppPage.Profile -> ProfileScreen(PaddingValues(0.dp))
                }
            }
        }
    }
}

@Composable
private fun AppNavigationRail(
    currentPage: AppPage,
    onPageSelected: (AppPage) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .padding(start = 10.dp, top = 12.dp, bottom = 12.dp)
            .width(92.dp),
        containerColor = Color(0xFFEAF7EE),
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DensityMedium,
                        contentDescription = "Open menu",
                        tint = Color(0xFF3F6150)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFD6EFDD)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = "Electricity logo",
                        tint = Color(0xFF1A6A46)
                    )
                }
            }
        }
    ) {
        AppPage.entries.forEach { page ->
            NavigationRailItem(
                selected = currentPage == page,
                onClick = { onPageSelected(page) },
                icon = { Icon(page.icon, contentDescription = page.title) },
                label = { Text(page.title) },
                alwaysShowLabel = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppHeader() {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Energy Saver",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track usage, tips, and carbon goals",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF5A7663)
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDEEE1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF16593D)
                )
            }
        }
    )
}

@Composable
private fun HomeDashboardScreen(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8F3))
            .padding(innerPadding),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroSection(
                title = "A cleaner-energy dashboard built in true top-down layout",
                subtitle = "Header first, navigation second, then the full body unfolds into energy data, quick actions, live updates, and chart modules."
            )
        }

        item {
            SectionHeading("Main summary")
        }

        item {
            DashboardStatRow()
        }

        item {
            SectionHeading("Body modules")
        }

        item {
            UsageChartCard()
        }

        item {
            DualPanelRow(
                left = {
                    ActionPanel(
                        title = "Best action now",
                        text = "Delay laundry until 2 PM to reduce peak-hour consumption and lower your estimated carbon impact.",
                        badge = "Recommended"
                    )
                },
                right = {
                    ActionPanel(
                        title = "Today's eco target",
                        text = "Stay under 4.0 kWh and keep your cooling time within the planned range.",
                        badge = "In progress"
                    )
                }
            )
        }

        item {
            SectionHeading("Recent activity")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FeedCard(
                    title = "Usage spike detected",
                    text = "Air conditioning usage was higher than usual between 1 PM and 4 PM."
                )
                FeedCard(
                    title = "Tip unlocked",
                    text = "Switching standby devices off overnight could save around 6% this week."
                )
                FeedCard(
                    title = "Goal progress updated",
                    text = "You are 72% of the way toward this week's CO2 reduction target."
                )
            }
        }
    }
}

@Composable
private fun InsightDashboardScreen(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8F3))
            .padding(innerPadding),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InsightHeaderCard()
        }

        item {
            SectionHeading("Trend and analysis")
        }

        item {
            InsightChartCard()
        }

        item {
            DualPanelRow(
                left = {
                    GoalCard(
                        title = "Carbon goal",
                        progress = "3.8 / 5.0 kg saved",
                        footer = "On track for this week"
                    )
                },
                right = {
                    GoalCard(
                        title = "Electricity budget",
                        progress = "19.4 / 24.0 kWh used",
                        footer = "Under your weekly limit"
                    )
                }
            )
        }

        item {
            SectionHeading("Supporting modules")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(
                    title = "Smart reminders",
                    text = "Remind users before peak-price periods and suggest lower-impact time windows."
                )
                ModuleCard(
                    title = "Weather-aware suggestions",
                    text = "Combine forecast data with home habits to adjust heating, cooling, and drying advice."
                )
                ModuleCard(
                    title = "Search and records",
                    text = "Let users search previous entries, appliance categories, and saved eco tips."
                )
            }
        }
    }
}

@Composable
private fun ProfileScreen(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8F3))
            .padding(innerPadding),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InsightHeaderCard(
                title = "Profile and preferences",
                description = "This page is reserved for the user form, household preferences, and the editable settings that will support personalisation later."
            )
        }

        item {
            SectionHeading("Form modules")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(
                    title = "Household type dropdown",
                    text = "Choose apartment, shared house, or family home to personalise energy guidance."
                )
                ModuleCard(
                    title = "Energy goal settings",
                    text = "Set weekly electricity and carbon targets for reminders and progress tracking."
                )
                ModuleCard(
                    title = "Reminder preferences",
                    text = "Configure when eco tips and scheduled alerts should appear."
                )
            }
        }
    }
}

@Composable
private fun HeroSection(title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF145A3D), Color(0xFF2B7B58))
                    )
                )
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.16f)
                ) {
                    Text(
                        text = "TOP-DOWN BODY LAYOUT",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color(0xFFE7F7EC),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun InsightHeaderCard(
    title: String = "Insights page",
    description: String = "Instead of explaining the idea, this page behaves like a real product body: chart section, goals, reminders, and system modules."
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF5A7663)
            )
            Text(
                text = "The second screen continues the same top-down structure with analysis-first modules.",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF183D2D)
            )
            Text(
                text = description,
                color = Color(0xFF5A7663)
            )
        }
    }
}

@Composable
private fun DashboardStatRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Today",
            value = "4.2 kWh",
            subtitle = "Lower than yesterday"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "CO2",
            value = "3.8 kg",
            subtitle = "Weekly goal on track"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, color = Color(0xFF5A7663), style = MaterialTheme.typography.labelLarge)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF183D2D))
            Text(subtitle, color = Color(0xFF5A7663), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun UsageChartCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(26.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Energy trend",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "This chart-like module sits in the body as a main content block, just like a dashboard homepage.",
                color = Color(0xFF5A7663)
            )
            MiniBarChart(
                values = listOf(0.45f, 0.72f, 0.58f, 0.82f, 0.64f, 0.52f, 0.34f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LegendDot(Color(0xFF1B6D49), "Usage")
                LegendDot(Color(0xFFBEDDC6), "Target")
            }
        }
    }
}

@Composable
private fun InsightChartCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(26.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Weekly carbon reduction",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "The body starts with the biggest analytical module, then moves down into goals and supporting content.",
                color = Color(0xFF5A7663)
            )
            MiniBarChart(
                values = listOf(0.32f, 0.48f, 0.67f, 0.61f, 0.73f, 0.84f, 0.79f)
            )
        }
    }
}

@Composable
private fun MiniBarChart(values: List<Float>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF5FBF6))
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        val barWidth = size.width / (values.size * 1.6f)
        val gap = barWidth * 0.6f
        val maxHeight = size.height * 0.8f

        values.forEachIndexed { index, value ->
            val left = index * (barWidth + gap) + gap
            val barHeight = maxHeight * value
            drawRoundRect(
                color = Color(0xFF1B6D49),
                topLeft = Offset(left, size.height - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
            )
        }
    }
}

@Composable
private fun LegendDot(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text, color = Color(0xFF5A7663), style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun DualPanelRow(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        left()
        right()
    }
}

@Composable
private fun ActionPanel(title: String, text: String, badge: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ModuleBadge(badge)
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text, color = Color(0xFF5A7663))
        }
    }
}

@Composable
private fun GoalCard(title: String, progress: String, footer: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = Color(0xFF5A7663))
            Text(progress, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFDCEEDF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFF1B6D49))
                )
            }
            Text(footer, color = Color(0xFF5A7663))
        }
    }
}

@Composable
private fun FeedCard(title: String, text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDF0E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.TipsAndUpdates,
                    contentDescription = null,
                    tint = Color(0xFF16593D)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(text, color = Color(0xFF5A7663))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF5A7663)
            )
        }
    }
}

@Composable
private fun ModuleCard(title: String, text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text, color = Color(0xFF5A7663))
        }
    }
}

@Composable
private fun ModuleBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color(0xFFDDF0E2)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = Color(0xFF16593D),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF183D2D)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EnergySaverPreview() {
    EnergySaverTheme {
        EnergySaverApp()
    }
}
