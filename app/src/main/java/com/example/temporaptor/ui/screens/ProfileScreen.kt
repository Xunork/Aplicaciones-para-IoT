package com.example.temporaptor.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temporaptor.SensorScanActivity
import com.example.temporaptor.ui.components.TerrariumNotificationDialog
import com.example.temporaptor.ui.components.TurtleHeadIcon
import com.example.temporaptor.ui.theme.EmeraldAccent

@Composable
fun ProfileScreen(
    userName: String,
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    onUpdateUserName: (String) -> Unit,
    onViewOnboarding: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showLegalDialog by remember { mutableStateOf<String?>(null) } // "terms" or "privacy"

    // Settings states
    var isCelsiusUnit by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var emailAlertsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("Español") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                TurtleHeadIcon(size = 48.dp)
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { showEditProfileDialog = true }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar Perfil",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = EmeraldAccent.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "🐢 Herpetólogo Pro • Conectado",
                            color = EmeraldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 1: Preferencias de Tema & Apariencia (Modo Claro / Oscuro)
            SectionHeader(title = "Apariencia & Tema de la App")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Dark / Light Theme Toggle Switch
                    SettingToggleRow(
                        title = "Tema de Pantalla (Claro / Oscuro)",
                        subtitle = if (isDarkTheme) "Modo Oscuro Reptil (Noche)" else "Modo Claro Terrario (Día)",
                        icon = if (isDarkTheme) Icons.Default.Nightlight else Icons.Default.WbSunny,
                        checked = isDarkTheme,
                        onCheckedChange = {
                            onToggleDarkTheme(it)
                            Toast.makeText(context, "Tema cambiado a ${if (it) "Modo Oscuro" else "Modo Claro"}", Toast.LENGTH_SHORT).show()
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    // Temperature Units Toggle
                    SettingToggleRow(
                        title = "Unidad de Temperatura",
                        subtitle = if (isCelsiusUnit) "Grados Celsius (°C)" else "Grados Fahrenheit (°F)",
                        icon = Icons.Default.Thermostat,
                        checked = isCelsiusUnit,
                        onCheckedChange = {
                            isCelsiusUnit = it
                            Toast.makeText(context, "Unidad cambiada a ${if (it) "°C" else "°F"}", Toast.LENGTH_SHORT).show()
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    // Language Preference
                    SettingActionRow(
                        title = "Idioma de la App",
                        valueText = selectedLanguage,
                        icon = Icons.Default.Language,
                        onClick = {
                            selectedLanguage = if (selectedLanguage == "Español") "English" else "Español"
                            Toast.makeText(context, "Idioma establecido en $selectedLanguage", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 2: Permisos en Android & IoT
            SectionHeader(title = "Permisos en Android & Sensores")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SettingActionRow(
                        title = "Escaneo de Sensores (SensorScanActivity)",
                        valueText = "Abrir Activity",
                        icon = Icons.Default.Bluetooth,
                        onClick = {
                            val intent = Intent(context, SensorScanActivity::class.java)
                            context.startActivity(intent)
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    SettingActionRow(
                        title = "Permiso de Ubicación & BLE",
                        valueText = "Activo",
                        icon = Icons.Default.LocationOn,
                        onClick = {
                            Toast.makeText(context, "Permiso de ubicación activo para bluetooth BLE", Toast.LENGTH_SHORT).show()
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    SettingActionRow(
                        title = "Permiso de Cámara QR",
                        valueText = "Activo",
                        icon = Icons.Default.CameraAlt,
                        onClick = {
                            Toast.makeText(context, "Permiso de cámara disponible para código QR de terrarios", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 3: Notificaciones Emergentes & Alertas
            SectionHeader(title = "Notificaciones Emergentes & Alertas")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SettingActionRow(
                        title = "Ver Ventana Emergente de Alertas",
                        valueText = "Probar Pop-Up",
                        icon = Icons.Default.NotificationsActive,
                        onClick = { showNotificationsDialog = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    SettingToggleRow(
                        title = "Notificaciones PUSH",
                        subtitle = "Pop-ups inmediatos en el móvil por lecturas fuera de rango",
                        icon = Icons.Default.NotificationsActive,
                        checked = pushNotificationsEnabled,
                        onCheckedChange = { pushNotificationsEnabled = it }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    SettingToggleRow(
                        title = "Alertas por Correo",
                        subtitle = "Resumen diario de salud del terrario e historial",
                        icon = Icons.Default.Sensors,
                        checked = emailAlertsEnabled,
                        onCheckedChange = { emailAlertsEnabled = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 4: Legal, Tutorial & Ayuda
            SectionHeader(title = "Soporte & Información Legal")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SettingActionRow(
                        title = "Ver Tutorial (Onboarding)",
                        valueText = "Guía inicial",
                        icon = Icons.Default.School,
                        onClick = onViewOnboarding
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    SettingActionRow(
                        title = "Términos de Servicio",
                        valueText = "Leer",
                        icon = Icons.Default.Description,
                        onClick = { showLegalDialog = "terms" }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    SettingActionRow(
                        title = "Política de Privacidad",
                        valueText = "Leer",
                        icon = Icons.Default.Lock,
                        onClick = { showLegalDialog = "privacy" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 5: Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // ==================== NOTIFICATIONS POPUP DIALOG ====================
        if (showNotificationsDialog) {
            TerrariumNotificationDialog(
                onDismiss = { showNotificationsDialog = false }
            )
        }

        // ==================== EDIT PROFILE DIALOG ====================
        if (showEditProfileDialog) {
            var tempName by remember { mutableStateOf(userName) }
            AlertDialog(
                onDismissRequest = { showEditProfileDialog = false },
                title = { Text("Editar Nombre de Perfil", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Ingresa tu nuevo nombre de usuario herpetólogo:")
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Nombre Completo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (tempName.isNotBlank()) {
                                onUpdateUserName(tempName)
                                Toast.makeText(context, "Nombre actualizado con éxito", Toast.LENGTH_SHORT).show()
                            }
                            showEditProfileDialog = false
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditProfileDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // ==================== LOGOUT CONFIRMATION DIALOG ====================
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("¿Cerrar Sesión en TempoRaptor?", fontWeight = FontWeight.Bold) },
                text = { Text("¿Estás seguro de que deseas salir? Las automatizaciones de tus terrarios continuarán ejecutándose en la nube.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar Sesión", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // ==================== LEGAL DIALOG ====================
        if (showLegalDialog != null) {
            AlertDialog(
                onDismissRequest = { showLegalDialog = null },
                title = {
                    Text(
                        text = if (showLegalDialog == "terms") "Términos de Servicio" else "Política de Privacidad",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = if (showLegalDialog == "terms")
                            "Al utilizar TempoRaptor, te comprometes a proveer condiciones óptimas de bienestar para tus reptiles, asegurando lecturas precisas mediante sensores calibrados."
                        else
                            "TempoRaptor procesa datos de sensores ambientalistas (temperatura y humedad) para ejecutar acciones automatizadas. Tus datos personales y la privacidad de tu hábitat están encriptados.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(onClick = { showLegalDialog = null }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun SettingToggleRow(
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingActionRow(
    title: String,
    valueText: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
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

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = valueText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
