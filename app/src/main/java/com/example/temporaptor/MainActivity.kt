package com.example.temporaptor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.temporaptor.data.MockReptileData
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
            TemporaptorTheme {
                MainAppController()
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
    val icon: ImageVector
) {
    HOME("Terrario", Icons.Default.Pets),
    ANALYTICS("Historial", Icons.Default.BarChart),
    SPECIES("Especies", Icons.Default.MenuBook),
    AUTOMATION("Alertas", Icons.Default.Tune),
    PROFILE("Perfil", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppController() {
    val context = LocalContext.current

    var rootScreen by rememberSaveable { mutableStateOf(RootAppScreen.SPLASH) }
    var currentDestination by rememberSaveable { mutableStateOf(TempoRaptorDestinations.HOME) }
    var loggedInUser by rememberSaveable { mutableStateOf("Herpetólogo Pro") }

    // App State: Terrariums
    var terrariums by remember { mutableStateOf(MockReptileData.sampleTerrariums) }
    var selectedTerrariumId by rememberSaveable { mutableStateOf(terrariums.first().id) }

    val selectedTerrarium = terrariums.find { it.id == selectedTerrariumId } ?: terrariums.first()

    when (rootScreen) {
        RootAppScreen.SPLASH -> {
            SplashScreen(
                onSplashFinished = {
                    rootScreen = RootAppScreen.ONBOARDING
                }
            )
        }

        RootAppScreen.ONBOARDING -> {
            OnboardingScreen(
                onOnboardingFinished = {
                    rootScreen = RootAppScreen.AUTH
                }
            )
        }

        RootAppScreen.AUTH -> {
            AuthScreen(
                onLoginSuccess = { name ->
                    loggedInUser = name
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
                            Text(
                                text = "Hola, $loggedInUser",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
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
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.label
                                    )
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
                                onToggleHeatLamp = { active ->
                                    terrariums = terrariums.map {
                                        if (it.id == selectedTerrarium.id) it.copy(heatLampActive = active) else it
                                    }
                                    val msg = if (active) "Lámpara de calor activada" else "Lámpara de calor apagada"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                },
                                onToggleMisting = { active ->
                                    terrariums = terrariums.map {
                                        if (it.id == selectedTerrarium.id) it.copy(mistingActive = active) else it
                                    }
                                    val msg = if (active) "Nebulizador de aire activado" else "Nebulizador detenido"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                },
                                onToggleSoilIrrigation = { active ->
                                    terrariums = terrariums.map {
                                        if (it.id == selectedTerrarium.id) it.copy(soilIrrigationActive = active) else it
                                    }
                                    val msg = if (active) "Riego de sustrato activado" else "Riego de sustrato detenido"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                },
                                onToggleUvLight = { active ->
                                    terrariums = terrariums.map {
                                        if (it.id == selectedTerrarium.id) it.copy(uvLightActive = active) else it
                                    }
                                    val msg = if (active) "Luz UVB 10.0 activada" else "Luz UVB apagada"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        TempoRaptorDestinations.ANALYTICS -> {
                            AnalyticsScreen(
                                selectedTerrarium = selectedTerrarium,
                                historyData = MockReptileData.historyData24h,
                                recentAlerts = MockReptileData.recentAlerts
                            )
                        }

                        TempoRaptorDestinations.SPECIES -> {
                            SpeciesGuideScreen(
                                speciesList = MockReptileData.speciesCatalog,
                                onApplySpeciesToTerrarium = { species ->
                                    terrariums = terrariums.map {
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
                                    Toast.makeText(
                                        context,
                                        "Parámetros de ${species.commonName} aplicados a ${selectedTerrarium.reptileName}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    currentDestination = TempoRaptorDestinations.HOME
                                }
                            )
                        }

                        TempoRaptorDestinations.AUTOMATION -> {
                            AutomationScreen(
                                terrarium = selectedTerrarium,
                                onSaveThresholds = { minTemp, maxTemp, minAir, maxAir, minSoil, maxSoil ->
                                    terrariums = terrariums.map {
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
                                    Toast.makeText(context, "Umbrales actualizados con éxito", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        TempoRaptorDestinations.PROFILE -> {
                            ProfileScreen(
                                userName = loggedInUser,
                                onUpdateUserName = { newName ->
                                    loggedInUser = newName
                                },
                                onViewOnboarding = {
                                    rootScreen = RootAppScreen.ONBOARDING
                                },
                                onLogout = {
                                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    TemporaptorTheme {
        MainAppController()
    }
}
