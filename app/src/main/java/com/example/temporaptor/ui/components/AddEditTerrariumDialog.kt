package com.example.temporaptor.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temporaptor.data.Terrarium
import java.util.UUID

/**
 * Dialog for Adding or Modifying a Reptile & Terrarium environment.
 */
@Composable
fun AddEditTerrariumDialog(
    initialTerrarium: Terrarium? = null,
    onDismiss: () -> Unit,
    onSaveTerrarium: (Terrarium) -> Unit
) {
    val context = LocalContext.current
    val isEditing = initialTerrarium != null

    var reptileName by remember { mutableStateOf(initialTerrarium?.reptileName ?: "") }
    var species by remember { mutableStateOf(initialTerrarium?.species ?: "") }
    var terrariumName by remember { mutableStateOf(initialTerrarium?.name ?: "") }

    var minTemp by remember { mutableStateOf(initialTerrarium?.minTemp?.toString() ?: "22.0") }
    var maxTemp by remember { mutableStateOf(initialTerrarium?.maxTemp?.toString() ?: "28.0") }

    var minAirHumidity by remember { mutableStateOf(initialTerrarium?.minAirHumidity?.toString() ?: "60.0") }
    var maxAirHumidity by remember { mutableStateOf(initialTerrarium?.maxAirHumidity?.toString() ?: "80.0") }

    var minSoilHumidity by remember { mutableStateOf(initialTerrarium?.minSoilHumidity?.toString() ?: "50.0") }
    var maxSoilHumidity by remember { mutableStateOf(initialTerrarium?.maxSoilHumidity?.toString() ?: "70.0") }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (reptileName.isBlank() || species.isBlank() || terrariumName.isBlank()) {
                        Toast.makeText(context, "Por favor completa el nombre del reptil y especie", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val parsedMinTemp = minTemp.toFloatOrNull() ?: 22.0f
                    val parsedMaxTemp = maxTemp.toFloatOrNull() ?: 28.0f
                    val parsedMinAir = minAirHumidity.toFloatOrNull() ?: 60.0f
                    val parsedMaxAir = maxAirHumidity.toFloatOrNull() ?: 80.0f
                    val parsedMinSoil = minSoilHumidity.toFloatOrNull() ?: 50.0f
                    val parsedMaxSoil = maxSoilHumidity.toFloatOrNull() ?: 70.0f

                    val savedTerrarium = Terrarium(
                        id = initialTerrarium?.id ?: UUID.randomUUID().toString(),
                        name = terrariumName,
                        reptileName = reptileName,
                        species = species,
                        currentTemp = initialTerrarium?.currentTemp ?: ((parsedMinTemp + parsedMaxTemp) / 2f),
                        minTemp = parsedMinTemp,
                        maxTemp = parsedMaxTemp,
                        currentAirHumidity = initialTerrarium?.currentAirHumidity ?: ((parsedMinAir + parsedMaxAir) / 2f),
                        minAirHumidity = parsedMinAir,
                        maxAirHumidity = parsedMaxAir,
                        currentSoilHumidity = initialTerrarium?.currentSoilHumidity ?: ((parsedMinSoil + parsedMaxSoil) / 2f),
                        minSoilHumidity = parsedMinSoil,
                        maxSoilHumidity = parsedMaxSoil,
                        heatLampActive = initialTerrarium?.heatLampActive ?: true,
                        mistingActive = initialTerrarium?.mistingActive ?: false,
                        soilIrrigationActive = initialTerrarium?.soilIrrigationActive ?: false,
                        uvLightActive = initialTerrarium?.uvLightActive ?: true
                    )

                    onSaveTerrarium(savedTerrarium)
                    Toast.makeText(
                        context,
                        if (isEditing) "Mascota $reptileName actualizada" else "Reptil $reptileName agregado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (isEditing) "Guardar Cambios" else "Agregar Reptil",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TurtleHeadIcon(size = 28.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isEditing) "Modificar Reptil" else "Agregar Nuevo Reptil",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Datos de la Mascota",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reptileName,
                    onValueChange = { reptileName = it },
                    label = { Text("Nombre del Reptil") },
                    placeholder = { Text("Ej. Draco, Iggy, Kaa") },
                    leadingIcon = { Icon(Icons.Default.Pets, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = species,
                    onValueChange = { species = it },
                    label = { Text("Especie") },
                    placeholder = { Text("Ej. Gecko Crestado, Dragón Barbudo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = terrariumName,
                    onValueChange = { terrariumName = it },
                    label = { Text("Nombre del Terrario") },
                    placeholder = { Text("Ej. Terrario Tropical A1") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rango de Temperatura Deseada (°C)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = minTemp,
                        onValueChange = { minTemp = it },
                        label = { Text("Mínima °C") },
                        leadingIcon = { Icon(Icons.Default.Thermostat, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = maxTemp,
                        onValueChange = { maxTemp = it },
                        label = { Text("Máxima °C") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rango de Humedad del Aire (%)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = minAirHumidity,
                        onValueChange = { minAirHumidity = it },
                        label = { Text("Mínima %") },
                        leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = maxAirHumidity,
                        onValueChange = { maxAirHumidity = it },
                        label = { Text("Máxima %") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rango de Humedad del Sustrato Tierra (%)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = minSoilHumidity,
                        onValueChange = { minSoilHumidity = it },
                        label = { Text("Mínima %") },
                        leadingIcon = { Icon(Icons.Default.Grass, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = maxSoilHumidity,
                        onValueChange = { maxSoilHumidity = it },
                        label = { Text("Máxima %") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }
    )
}
