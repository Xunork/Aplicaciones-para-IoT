package com.example.temporaptor.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import java.util.Locale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temporaptor.data.AlertEvent
import com.example.temporaptor.data.EnvironmentalHistoryPoint
import com.example.temporaptor.data.Terrarium
import com.example.temporaptor.ui.theme.AirHumidityBlue
import com.example.temporaptor.ui.theme.SoilMoistureCyan
import com.example.temporaptor.ui.theme.TempOrange

@Composable
fun AnalyticsScreen(
    selectedTerrarium: Terrarium,
    historyData: List<EnvironmentalHistoryPoint>,
    recentAlerts: List<AlertEvent>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedMetricTab by remember { mutableIntStateOf(0) } // 0: Temp, 1: Air Hum, 2: Soil Hum
    var selectedTimeframe by remember { mutableStateOf("24 Hours") }

    val activeColor = when (selectedMetricTab) {
        0 -> TempOrange
        1 -> AirHumidityBlue
        else -> SoilMoistureCyan
    }

    val activeUnit = when (selectedMetricTab) {
        0 -> "°C"
        else -> "%"
    }

    val activeMetricName = when (selectedMetricTab) {
        0 -> "Temperatura del Aire"
        1 -> "Humedad del Aire"
        else -> "Humedad de Tierra (Sustrato)"
    }

    val activePoints = historyData.map { point ->
        when (selectedMetricTab) {
            0 -> point.temp
            1 -> point.airHumidity
            else -> point.soilHumidity
        }
    }

    val avgVal = if (activePoints.isNotEmpty()) activePoints.average().toFloat() else 0f
    val maxVal = activePoints.maxOrNull() ?: 0f
    val minVal = activePoints.minOrNull() ?: 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Historial & Analíticas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Evolución climática de ${selectedTerrarium.reptileName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Gráficos",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timeframe selector chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("24 Horas", "7 Días", "30 Días").forEach { label ->
                FilterChip(
                    selected = selectedTimeframe == label,
                    onClick = { selectedTimeframe = label },
                    label = { Text(label, fontSize = 12.sp) },
                    shape = RoundedCornerShape(50)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Metric selector tabs
        PrimaryTabRow(
            selectedTabIndex = selectedMetricTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedMetricTab == 0,
                onClick = { selectedMetricTab = 0 },
                text = { Text("Temperatura", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Thermostat, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedMetricTab == 1,
                onClick = { selectedMetricTab = 1 },
                text = { Text("Humedad Aire", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.WaterDrop, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedMetricTab == 2,
                onClick = { selectedMetricTab = 2 },
                text = { Text("Humedad Tierra", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Grass, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Canvas Line Chart Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = activeMetricName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Gráfico $selectedTimeframe",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "Prom: ${String.format(Locale.getDefault(), "%.1f", avgVal)}$activeUnit",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = activeColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Line Chart Canvas
                CustomMetricChart(
                    points = activePoints,
                    labels = historyData.map { it.timeLabel },
                    lineColor = activeColor,
                    unit = activeUnit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Stats Summary Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Máximo Registrado",
                value = "${String.format(Locale.getDefault(), "%.1f", maxVal)}$activeUnit",
                color = activeColor,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Mínimo Registrado",
                value = "${String.format(Locale.getDefault(), "%.1f", minVal)}$activeUnit",
                color = activeColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Event Log & Recent Alerts Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Historial de Eventos & Automatizaciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        recentAlerts.forEach { alert ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .padding(top = 4.dp)
                            .background(if (alert.isCritical) MaterialTheme.colorScheme.error else activeColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = alert.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = alert.timestamp,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = alert.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomMetricChart(
    points: List<Float>,
    labels: List<String>,
    lineColor: Color,
    unit: String,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val minP = (points.minOrNull() ?: 0f) - 2f
    val maxP = (points.maxOrNull() ?: 100f) + 2f
    val rangeP = if (maxP - minP == 0f) 1f else maxP - minP

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height - 30f // Reserve bottom space for time labels

        val stepX = width / (points.size - 1).coerceAtLeast(1)

        // Draw horizontal grid lines
        val gridLines = 3
        for (i in 0..gridLines) {
            val y = height * (i.toFloat() / gridLines)
            drawLine(
                color = lineColor.copy(alpha = 0.15f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        // Build chart line path
        val path = Path()
        points.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedY = (value - minP) / rangeP
            val y = height - (normalizedY * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // Draw line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw data points
        points.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedY = (value - minP) / rangeP
            val y = height - (normalizedY * height)

            drawCircle(
                color = lineColor,
                radius = 5.dp.toPx(),
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White,
                radius = 2.5.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
