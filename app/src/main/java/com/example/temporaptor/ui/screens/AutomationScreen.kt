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
import androidx.compose.material3.Slider
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

        // Section 1: Threshold Sliders
        Text(
            text = "Umbrales Objetivo del Terrario",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Temperature Slider Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Thermostat, contentDescription = null, tint = TempOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rango Temperatura", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "${String.format(Locale.getDefault(), "%.1f", minTemp)}° - ${String.format(Locale.getDefault(), "%.1f", maxTemp)}°C",
                        fontWeight = FontWeight.Bold,
                        color = TempOrange
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                RangeSlider(
                    value = minTemp..maxTemp,
                    onValueChange = { range ->
                        minTemp = range.start
                        maxTemp = range.endInclusive
                    },
                    valueRange = 15f..42f,
                    steps = 54
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Air Humidity Slider Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = AirHumidityBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Humedad del Aire", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "${minAir.toInt()}% - ${maxAir.toInt()}%",
                        fontWeight = FontWeight.Bold,
                        color = AirHumidityBlue
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                RangeSlider(
                    value = minAir..maxAir,
                    onValueChange = { range ->
                        minAir = range.start
                        maxAir = range.endInclusive
                    },
                    valueRange = 20f..95f,
                    steps = 75
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Soil Humidity Slider Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Grass, contentDescription = null, tint = SoilMoistureCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Humedad de Tierra (Sustrato)", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "${minSoil.toInt()}% - ${maxSoil.toInt()}%",
                        fontWeight = FontWeight.Bold,
                        color = SoilMoistureCyan
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                RangeSlider(
                    value = minSoil..maxSoil,
                    onValueChange = { range ->
                        minSoil = range.start
                        maxSoil = range.endInclusive
                    },
                    valueRange = 10f..90f,
                    steps = 80
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSaveThresholds(minTemp, maxTemp, minAir, maxAir, minSoil, maxSoil)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Guardar Umbrales Objetivo", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 2: Smart Automations
        Text(
            text = "Reglas de Automatización Inteligente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        AutomationSwitchRow(
            title = "Auto-Nebulización de Aire",
            subtitle = "Activar nebulizador automáticamente si la humedad ambiental cae de ${minAir.toInt()}%",
            icon = Icons.Default.Opacity,
            iconTint = AirHumidityBlue,
            checked = autoMistEnabled,
            onCheckedChange = { autoMistEnabled = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AutomationSwitchRow(
            title = "Auto-Riego de Sustrato",
            subtitle = "Goteo automático si la humedad de la tierra cae de ${minSoil.toInt()}%",
            icon = Icons.Default.Grass,
            iconTint = SoilMoistureCyan,
            checked = autoSoilIrrigationEnabled,
            onCheckedChange = { autoSoilIrrigationEnabled = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AutomationSwitchRow(
            title = "Corte Térmico de Seguridad",
            subtitle = "Apagar lámpara de calor si la temperatura supera los $maxTemp°C",
            icon = Icons.Default.Thermostat,
            iconTint = TempOrange,
            checked = thermalCutoffEnabled,
            onCheckedChange = { thermalCutoffEnabled = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AutomationSwitchRow(
            title = "Descenso Térmico Nocturno",
            subtitle = "Bajar temperatura deseada en -3°C entre 22:00 y 07:00",
            icon = Icons.Default.Nightlight,
            iconTint = MaterialTheme.colorScheme.secondary,
            checked = nightModeEnabled,
            onCheckedChange = { nightModeEnabled = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AutomationSwitchRow(
            title = "Alertas PUSH en Celular",
            subtitle = "Notificar inmediatamente cuando un parámetro esté en estado Crítico",
            icon = Icons.Default.NotificationsActive,
            iconTint = MaterialTheme.colorScheme.primary,
            checked = pushAlertsEnabled,
            onCheckedChange = { pushAlertsEnabled = it }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AutomationSwitchRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = iconTint.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
}
