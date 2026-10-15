package com.example.temporaptor.ui.screens

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
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temporaptor.data.Terrarium
import com.example.temporaptor.ui.theme.AirHumidityBlue
import com.example.temporaptor.ui.theme.SoilMoistureCyan
import com.example.temporaptor.ui.theme.TempOrange
import java.util.Locale

@Composable
fun AutomationScreen(
    terrarium: Terrarium,
    onSaveThresholds: (minTemp: Float, maxTemp: Float, minAir: Float, maxAir: Float, minSoil: Float, maxSoil: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var minTemp by remember(terrarium) { mutableFloatStateOf(terrarium.minTemp) }
    var maxTemp by remember(terrarium) { mutableFloatStateOf(terrarium.maxTemp) }

    var minAir by remember(terrarium) { mutableFloatStateOf(terrarium.minAirHumidity) }
    var maxAir by remember(terrarium) { mutableFloatStateOf(terrarium.maxAirHumidity) }

    var minSoil by remember(terrarium) { mutableFloatStateOf(terrarium.minSoilHumidity) }
    var maxSoil by remember(terrarium) { mutableFloatStateOf(terrarium.maxSoilHumidity) }

    // Automation switches
    var autoMistEnabled by remember { mutableStateOf(true) }
    var autoSoilIrrigationEnabled by remember { mutableStateOf(true) }
    var thermalCutoffEnabled by remember { mutableStateOf(true) }
    var nightModeEnabled by remember { mutableStateOf(true) }
    var pushAlertsEnabled by remember { mutableStateOf(true) }

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
                    text = "Alertas & Automatización",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Ajusta umbrales y reglas para ${terrarium.reptileName}",
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
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Automatización",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Temperature Range Automation Slider Card
        ThresholdSliderCard(
            title = "Rango de Temperatura Ideal (°C)",
            subtitle = "Sonda Térmica Principal",
            icon = Icons.Default.Thermostat,
            accentColor = TempOrange,
            minValue = minTemp,
            maxValue = maxTemp,
            rangeBounds = 15f..45f,
            unit = "°C",
            onRangeChange = { newMin, newMax ->
                minTemp = newMin
                maxTemp = newMax
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Air Humidity Range Automation Slider Card
        ThresholdSliderCard(
            title = "Humedad del Aire Ideal (%)",
            subtitle = "Sensor Higrómetro Ambiental",
            icon = Icons.Default.WaterDrop,
            accentColor = AirHumidityBlue,
            minValue = minAir,
            maxValue = maxAir,
            rangeBounds = 20f..95f,
            unit = "%",
            onRangeChange = { newMin, newMax ->
                minAir = newMin
                maxAir = newMax
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Substrate Soil Humidity Range Automation Slider Card
        ThresholdSliderCard(
            title = "Humedad de Tierra (Sustrato) (%)",
            subtitle = "Sensor Capacitivo de Suelo",
            icon = Icons.Default.Grass,
            accentColor = SoilMoistureCyan,
            minValue = minSoil,
            maxValue = maxSoil,
            rangeBounds = 10f..90f,
            unit = "%",
            onRangeChange = { newMin, newMax ->
                minSoil = newMin
                maxSoil = newMax
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Save Thresholds CTA Button
        Button(
            onClick = {
                onSaveThresholds(minTemp, maxTemp, minAir, maxAir, minSoil, maxSoil)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Guardar Nuevos Umbrales de Automatización",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section Title: Automation Regules & Switches
        Text(
            text = "Reglas de Automatización IoT",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AutomationToggleRow(
                    title = "Lluvia / Nebulización Automática",
                    subtitle = "Activa nebulizador si la humedad del aire cae del mínimo",
                    icon = Icons.Default.Opacity,
                    checked = autoMistEnabled,
                    onCheckedChange = { autoMistEnabled = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                AutomationToggleRow(
                    title = "Riego Automático de Sustrato",
                    subtitle = "Humedece la tierra mediante goteo inteligente si está seca",
                    icon = Icons.Default.Grass,
                    checked = autoSoilIrrigationEnabled,
                    onCheckedChange = { autoSoilIrrigationEnabled = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                AutomationToggleRow(
                    title = "Corte Térmico de Seguridad",
                    subtitle = "Apaga lámparas de calor si se supera la temperatura máxima",
                    icon = Icons.Default.Thermostat,
                    checked = thermalCutoffEnabled,
                    onCheckedChange = { thermalCutoffEnabled = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                AutomationToggleRow(
                    title = "Modo Ciclo Nocturno",
                    subtitle = "Ajusta la temperatura 3°C más baja durante la noche",
                    icon = Icons.Default.Nightlight,
                    checked = nightModeEnabled,
                    onCheckedChange = { nightModeEnabled = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                AutomationToggleRow(
                    title = "Alertas Emergentes al Móvil",
                    subtitle = "Avisos instantáneos ante fallos o variaciones bruscas",
                    icon = Icons.Default.NotificationsActive,
                    checked = pushAlertsEnabled,
                    onCheckedChange = { pushAlertsEnabled = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ThresholdSliderCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    minValue: Float,
    maxValue: Float,
    rangeBounds: ClosedFloatingPointRange<Float>,
    unit: String,
    onRangeChange: (newMin: Float, newMax: Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = accentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Values Display Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "Mínimo: %.1f%s", minValue, unit),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )

                Text(
                    text = String.format(Locale.getDefault(), "Máximo: %.1f%s", maxValue, unit),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dual Slider (RangeSlider)
            RangeSlider(
                value = minValue..maxValue,
                onValueChange = { range ->
                    onRangeChange(range.start, range.endInclusive)
                },
                valueRange = rangeBounds,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AutomationToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
