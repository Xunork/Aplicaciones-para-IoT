package com.example.temporaptor.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temporaptor.data.SensorStatus
import com.example.temporaptor.data.Terrarium
import com.example.temporaptor.ui.components.QuickControlCard
import com.example.temporaptor.ui.components.SensorCard
import com.example.temporaptor.ui.theme.AirHumidityBlue
import com.example.temporaptor.ui.theme.EmeraldAccent
import com.example.temporaptor.ui.theme.JungleGreenPrimary
import com.example.temporaptor.ui.theme.SoilBrown
import com.example.temporaptor.ui.theme.SoilMoistureCyan
import com.example.temporaptor.ui.theme.TempOrange
import com.example.temporaptor.ui.theme.WarmTerracotta

@Composable
fun HomeScreen(
    terrariums: List<Terrarium>,
    selectedTerrarium: Terrarium,
    onSelectTerrarium: (Terrarium) -> Unit,
    onToggleHeatLamp: (Boolean) -> Unit,
    onToggleMisting: (Boolean) -> Unit,
    onToggleSoilIrrigation: (Boolean) -> Unit,
    onToggleUvLight: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val isAllOptimal = selectedTerrarium.tempStatus == SensorStatus.OPTIMAL &&
            selectedTerrarium.airHumidityStatus == SensorStatus.OPTIMAL &&
            selectedTerrarium.soilHumidityStatus == SensorStatus.OPTIMAL

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // App Header Branding
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = "TempoRaptor",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "TempoRaptor",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Gestión de Clima & Sustrato",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        Toast.makeText(context, "Sistemas y sensores sin novedades", Toast.LENGTH_SHORT).show()
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Terrarium Selector Tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(terrariums) { terrarium ->
                val isSelected = terrarium.id == selectedTerrarium.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectTerrarium(terrarium) },
                    label = {
                        Text(
                            text = "${terrarium.reptileName} (${terrarium.name})",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==================== PRIMARY HERO CTA CARD ====================
        // Prioritizes the central action the user needs to perform!
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                JungleGreenPrimary,
                                Color(0xFF0F3822)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = selectedTerrarium.reptileName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = selectedTerrarium.species,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isAllOptimal) EmeraldAccent.copy(alpha = 0.25f) else WarmTerracotta.copy(alpha = 0.35f)
                        ) {
                            Text(
                                text = if (isAllOptimal) "✓ Ambiente Estable" else "⚠️ Revisar Humedad",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mini summary metrics bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TerrariumQuickStat(
                            label = "Temperatura",
                            value = "${selectedTerrarium.currentTemp}°C",
                            icon = Icons.Default.Thermostat
                        )
                        TerrariumQuickStat(
                            label = "Humedad Aire",
                            value = "${selectedTerrarium.currentAirHumidity.toInt()}%",
                            icon = Icons.Default.WaterDrop
                        )
                        TerrariumQuickStat(
                            label = "Humedad Tierra",
                            value = "${selectedTerrarium.currentSoilHumidity.toInt()}%",
                            icon = Icons.Default.Grass
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // CENTRAL PRIMARY ACTION CTA BUTTON
                    Button(
                        onClick = {
                            if (!selectedTerrarium.mistingActive) onToggleMisting(true)
                            if (!selectedTerrarium.heatLampActive) onToggleHeatLamp(true)
                            Toast.makeText(
                                context,
                                "⚡ Ciclo de Clima & Humedad Inteligente activado para ${selectedTerrarium.reptileName}",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAllOptimal) "⚡ Estabilizar Hábitat Automáticamente" else "⚡ Corregir Parámetros del Hábitat",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: Real-time Sensors
        Text(
            text = "Parámetros en Tiempo Real",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Monitoreo de clima de aire y humedad de sustrato",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Temperature Sensor Card
        SensorCard(
            title = "Temperatura del Aire",
            subtitle = "Sonda Térmica Principal",
            valueText = "${selectedTerrarium.currentTemp}",
            unit = "°C",
            currentValue = selectedTerrarium.currentTemp,
            minValue = selectedTerrarium.minTemp,
            maxValue = selectedTerrarium.maxTemp,
            maxGaugeRange = 45.0f,
            status = selectedTerrarium.tempStatus,
            accentColor = TempOrange,
            icon = Icons.Default.Thermostat,
            actionButtonText = if (selectedTerrarium.heatLampActive) "Apagar Calefacción" else "Activar Lámpara de Calor",
            isDeviceActive = selectedTerrarium.heatLampActive,
            onToggleDevice = { onToggleHeatLamp(!selectedTerrarium.heatLampActive) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Air Humidity Sensor Card
        SensorCard(
            title = "Humedad del Aire (Ambiental)",
            subtitle = "Sensor Higrómetro Aéreo",
            valueText = "${selectedTerrarium.currentAirHumidity.toInt()}",
            unit = "%",
            currentValue = selectedTerrarium.currentAirHumidity,
            minValue = selectedTerrarium.minAirHumidity,
            maxValue = selectedTerrarium.maxAirHumidity,
            maxGaugeRange = 100.0f,
            status = selectedTerrarium.airHumidityStatus,
            accentColor = AirHumidityBlue,
            icon = Icons.Default.WaterDrop,
            actionButtonText = if (selectedTerrarium.mistingActive) "Detener Nebulizador" else "Activar Lluvia / Nebulizador",
            isDeviceActive = selectedTerrarium.mistingActive,
            onToggleDevice = { onToggleMisting(!selectedTerrarium.mistingActive) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Soil / Substrate Humidity Sensor Card
        SensorCard(
            title = "Humedad de Tierra (Sustrato)",
            subtitle = "Sensor de Tierra Capacitivo",
            valueText = "${selectedTerrarium.currentSoilHumidity.toInt()}",
            unit = "%",
            currentValue = selectedTerrarium.currentSoilHumidity,
            minValue = selectedTerrarium.minSoilHumidity,
            maxValue = selectedTerrarium.maxSoilHumidity,
            maxGaugeRange = 100.0f,
            status = selectedTerrarium.soilHumidityStatus,
            accentColor = SoilMoistureCyan,
            icon = Icons.Default.Grass,
            actionButtonText = if (selectedTerrarium.soilIrrigationActive) "Detener Riego" else "Activar Riego de Sustrato",
            isDeviceActive = selectedTerrarium.soilIrrigationActive,
            onToggleDevice = { onToggleSoilIrrigation(!selectedTerrarium.soilIrrigationActive) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: Quick Equipment Controls
        Text(
            text = "Control Rápido de Equipamiento",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickControlCard(
                title = "Lámpara Calor",
                subtitle = "Spot Cerámico",
                icon = Icons.Default.ElectricBolt,
                activeColor = TempOrange,
                isActive = selectedTerrarium.heatLampActive,
                onToggle = onToggleHeatLamp,
                modifier = Modifier.weight(1f)
            )

            QuickControlCard(
                title = "Nebulizador",
                subtitle = "Lluvia Fina",
                icon = Icons.Default.Opacity,
                activeColor = AirHumidityBlue,
                isActive = selectedTerrarium.mistingActive,
                onToggle = onToggleMisting,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickControlCard(
                title = "Riego Tierra",
                subtitle = "Goteo Sustrato",
                icon = Icons.Default.Grass,
                activeColor = SoilBrown,
                isActive = selectedTerrarium.soilIrrigationActive,
                onToggle = onToggleSoilIrrigation,
                modifier = Modifier.weight(1f)
            )

            QuickControlCard(
                title = "Luz UVB 10.0",
                subtitle = "Ciclo Diurno",
                icon = Icons.Default.WbSunny,
                activeColor = EmeraldAccent,
                isActive = selectedTerrarium.uvLightActive,
                onToggle = onToggleUvLight,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TerrariumQuickStat(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
