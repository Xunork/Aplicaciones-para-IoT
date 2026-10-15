package com.example.temporaptor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.temporaptor.data.AppPreferencesManager
import com.example.temporaptor.data.MockReptileData
import com.example.temporaptor.data.Terrarium
import com.example.temporaptor.ui.components.TurtleHeadIcon
import com.example.temporaptor.ui.screens.AnalyticsScreen
import com.example.temporaptor.ui.screens.AuthScreen
import com.example.temporaptor.ui.screens.AutomationScreen
import com.example.temporaptor.ui.screens.HomeScreen
import com.example.temporaptor.ui.screens.OnboardingScreen
import com.example.temporaptor.ui.screens.ProfileScreen
import com.example.temporaptor.ui.screens.SpeciesGuideScreen
import com.example.temporaptor.ui.screens.SplashScreen
import com.example.temporaptor.ui.theme.TemporaptorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(true) }
            TemporaptorTheme(darkTheme = isDarkTheme) {
                MainAppController(
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = { isDarkTheme = it }
                )
            }
        }
    }
}

enum class RootAppScreen {
    SPLASH,
    ONBOARDING,
    AUTH,
    MAIN
}

enum class TempoRaptorDestinations(
    val label: String,
    val icon: ImageVector?
) {
    HOME("Terrario", null),
    ANALYTICS("Historial", Icons.Default.BarChart),
    SPECIES("Especies", Icons.Default.MenuBook),
    AUTOMATION("Alertas", Icons.Default.Tune),
    PROFILE("Perfil", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppController(
    isDarkTheme: Boolean = true,
    onToggleDarkTheme: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    var rootScreen by rememberSaveable { mutableStateOf(RootAppScreen.SPLASH) }
    var currentDestination by rememberSaveable { mutableStateOf(TempoRaptorDestinations.HOME) }
    var loggedInUser by rememberSaveable { mutableStateOf("Herpetólogo Pro") }

    // Persistent Terrariums State (Loaded from SharedPreferences)
    var terrariums by remember {
        mutableStateOf(
            AppPreferencesManager.loadTerrariums(context) ?: MockReptileData.sampleTerrariums
        )
    }
    var selectedTerrariumId by rememberSaveable { mutableStateOf(terrariums.firstOrNull()?.id ?: "") }

    val selectedTerrarium = terrariums.find { it.id == selectedTerrariumId } ?: terrariums.firstOrNull()

    // Helper to update state and immediately persist to SharedPreferences
    val updateAndSaveTerrariums: (List<Terrarium>) -> Unit = { newList ->
        terrariums = newList
        AppPreferencesManager.saveTerrariums(context, newList)
    }

    when (rootScreen) {
        RootAppScreen.SPLASH -> {
            SplashScreen(
                onSplashFinished = {
                    val onboardingDone = AppPreferencesManager.isOnboardingDone(context)
                    val loggedIn = AppPreferencesManager.isLoggedIn(context)

                    if (!onboardingDone) {
                        rootScreen = RootAppScreen.ONBOARDING
                    } else if (loggedIn) {
                        loggedInUser = AppPreferencesManager.getLoggedUser(context)
                        // Load saved terrariums from disk
                        val savedList = AppPreferencesManager.loadTerrariums(context)
                        if (savedList != null) {
                            terrariums = savedList
                            selectedTerrariumId = savedList.firstOrNull()?.id ?: ""
                        }
                        rootScreen = RootAppScreen.MAIN
                    } else {
                        rootScreen = RootAppScreen.AUTH
                    }
                }
            )
        }

        RootAppScreen.ONBOARDING -> {
            OnboardingScreen(
                onOnboardingFinished = {
                    AppPreferencesManager.setOnboardingDone(context, true)
                    rootScreen = RootAppScreen.AUTH
                }
            )
        }

        RootAppScreen.AUTH -> {
            AuthScreen(
                onLoginSuccess = { name, isNewRegistration ->
                    loggedInUser = name
                    AppPreferencesManager.setLoggedIn(context, true, name)

                    if (isNewRegistration) {
                        val emptyList = emptyList<Terrarium>()
                        updateAndSaveTerrariums(emptyList)
                        selectedTerrariumId = ""
                    } else {
                        val savedList = AppPreferencesManager.loadTerrariums(context) ?: MockReptileData.sampleTerrariums
                        updateAndSaveTerrariums(savedList)
                        selectedTerrariumId = savedList.firstOrNull()?.id ?: ""
                    }
                    rootScreen = RootAppScreen.MAIN
                }
            )
        }

        RootAppScreen.MAIN -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TurtleHeadIcon(size = 24.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Hola, $loggedInUser",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        actions = {
                            // Profile button
                            IconButton(
                                onClick = {
                                    currentDestination = TempoRaptorDestinations.PROFILE
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Mi Perfil"
                                )
                            }

                            // Button to replay onboarding tutorial
                            IconButton(
                                onClick = {
                                    rootScreen = RootAppScreen.ONBOARDING
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Ver Onboarding"
                                )
                            }

                            // Logout button
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                                    AppPreferencesManager.setLoggedIn(context, false)
                                    rootScreen = RootAppScreen.AUTH
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "Cerrar Sesión"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                },
                bottomBar = {
                    NavigationBar {
                        TempoRaptorDestinations.entries.forEach { destination ->
                            NavigationBarItem(
                                selected = currentDestination == destination,
                                onClick = { currentDestination = destination },
                                icon = {
                                    if (destination == TempoRaptorDestinations.HOME) {
                                        TurtleHeadIcon(size = 22.dp)
                                    } else {
                                        destination.icon?.let { iconVector ->
                                            Icon(
                                                imageVector = iconVector,
                                                contentDescription = destination.label
                                            )
                                        }
                                    }
                                },
                                label = { Text(destination.label) }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentDestination) {
                        TempoRaptorDestinations.HOME -> {
                            HomeScreen(
                                terrariums = terrariums,
                                selectedTerrarium = selectedTerrarium,
                                onSelectTerrarium = { selectedTerrariumId = it.id },
                                onAddTerrarium = { newTerrarium ->
                                    val newList = terrariums + newTerrarium
                                    updateAndSaveTerrariums(newList)
                                    selectedTerrariumId = newTerrarium.id
                                },
                                onUpdateTerrarium = { updatedTerrarium ->
                                    val newList = terrariums.map { if (it.id == updatedTerrarium.id) updatedTerrarium else it }
                                    updateAndSaveTerrariums(newList)
                                },
                                onDeleteTerrarium = { deletedId ->
                                    val newList = terrariums.filter { it.id != deletedId }
                                    updateAndSaveTerrariums(newList)
                                    selectedTerrariumId = newList.firstOrNull()?.id ?: ""
                                },
                                onToggleHeatLamp = { active ->
                                    selectedTerrarium?.let { current ->
                                        val newList = terrariums.map {
                                            if (it.id == current.id) it.copy(heatLampActive = active) else it
                                        }
                                        updateAndSaveTerrariums(newList)
                                        val msg = if (active) "Lámpara de calor activada" else "Lámpara de calor apagada"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onToggleMisting = { active ->
                                    selectedTerrarium?.let { current ->
                                        val newList = terrariums.map {
                                            if (it.id == current.id) it.copy(mistingActive = active) else it
                                        }
                                        updateAndSaveTerrariums(newList)
                                        val msg = if (active) "Nebulizador de aire activado" else "Nebulizador detenido"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onToggleSoilIrrigation = { active ->
                                    selectedTerrarium?.let { current ->
                                        val newList = terrariums.map {
                                            if (it.id == current.id) it.copy(soilIrrigationActive = active) else it
                                        }
                                        updateAndSaveTerrariums(newList)
                                        val msg = if (active) "Riego de sustrato activado" else "Riego de sustrato detenido"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onToggleUvLight = { active ->
                                    selectedTerrarium?.let { current ->
                                        val newList = terrariums.map {
                                            if (it.id == current.id) it.copy(uvLightActive = active) else it
                                        }
                                        updateAndSaveTerrariums(newList)
                                        val msg = if (active) "Luz UVB 10.0 activada" else "Luz UVB apagada"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }

                        TempoRaptorDestinations.ANALYTICS -> {
                            if (selectedTerrarium != null) {
                                AnalyticsScreen(
                                    selectedTerrarium = selectedTerrarium,
                                    historyData = MockReptileData.historyData24h,
                                    recentAlerts = MockReptileData.recentAlerts
                                )
                            } else {
                                EmptyTerrariumSectionCard(onAddReptile = { currentDestination = TempoRaptorDestinations.HOME })
                            }
                        }

                        TempoRaptorDestinations.SPECIES -> {
                            SpeciesGuideScreen(
                                speciesList = MockReptileData.speciesCatalog,
                                onApplySpeciesToTerrarium = { species ->
                                    if (selectedTerrarium != null) {
                                        val newList = terrariums.map {
                                            if (it.id == selectedTerrarium.id) {
                                                it.copy(
                                                    species = "${species.commonName} (${species.scientificName})",
                                                    minTemp = species.recommendedTempMin,
                                                    maxTemp = species.recommendedTempMax,
                                                    minAirHumidity = species.recommendedAirHumMin,
                                                    maxAirHumidity = species.recommendedAirHumMax,
                                                    minSoilHumidity = species.recommendedSoilHumMin,
                                                    maxSoilHumidity = species.recommendedSoilHumMax
                                                )
                                            } else it
                                        }
                                        updateAndSaveTerrariums(newList)
                                        Toast.makeText(
                                            context,
                                            "Parámetros de ${species.commonName} aplicados a ${selectedTerrarium.reptileName}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(context, "Primero agrega un reptil para aplicar parámetros", Toast.LENGTH_SHORT).show()
                                    }
                                    currentDestination = TempoRaptorDestinations.HOME
                                }
                            )
                        }

                        TempoRaptorDestinations.AUTOMATION -> {
                            if (selectedTerrarium != null) {
                                AutomationScreen(
                                    terrarium = selectedTerrarium,
                                    onSaveThresholds = { minTemp, maxTemp, minAir, maxAir, minSoil, maxSoil ->
                                        val newList = terrariums.map {
                                            if (it.id == selectedTerrarium.id) {
                                                it.copy(
                                                    minTemp = minTemp,
                                                    maxTemp = maxTemp,
                                                    minAirHumidity = minAir,
                                                    maxAirHumidity = maxAir,
                                                    minSoilHumidity = minSoil,
                                                    maxSoilHumidity = maxSoil
                                                )
                                            } else it
                                        }
                                        updateAndSaveTerrariums(newList)
                                        Toast.makeText(context, "Umbrales actualizados con éxito", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            } else {
                                EmptyTerrariumSectionCard(onAddReptile = { currentDestination = TempoRaptorDestinations.HOME })
                            }
                        }

                        TempoRaptorDestinations.PROFILE -> {
                            ProfileScreen(
                                userName = loggedInUser,
                                isDarkTheme = isDarkTheme,
                                onToggleDarkTheme = onToggleDarkTheme,
                                onUpdateUserName = { newName ->
                                    loggedInUser = newName
                                    AppPreferencesManager.setLoggedIn(context, true, newName)
                                },
                                onViewOnboarding = {
                                    rootScreen = RootAppScreen.ONBOARDING
                                },
                                onLogout = {
                                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                                    AppPreferencesManager.setLoggedIn(context, false)
                                    rootScreen = RootAppScreen.AUTH
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTerrariumSectionCard(onAddReptile: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TurtleHeadIcon(size = 52.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Aún no tienes reptiles registrados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Para acceder a los histogramas y alertas de automatización, registra primero a tu reptil en el inicio.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAddReptile) {
                    Text("Ir al Inicio a Registrar Reptil")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    TemporaptorTheme {
        MainAppController()
    }
}
