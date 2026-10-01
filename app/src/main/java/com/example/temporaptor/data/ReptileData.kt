package com.example.temporaptor.data

import androidx.compose.ui.graphics.Color

enum class SensorStatus {
    OPTIMAL,
    WARNING,
    CRITICAL
}

data class Terrarium(
    val id: String,
    val name: String,
    val reptileName: String,
    val species: String,
    val currentTemp: Float, // °C
    val minTemp: Float,
    val maxTemp: Float,
    val currentAirHumidity: Float, // %
    val minAirHumidity: Float,
    val maxAirHumidity: Float,
    val currentSoilHumidity: Float, // %
    val minSoilHumidity: Float,
    val maxSoilHumidity: Float,
    val heatLampActive: Boolean,
    val mistingActive: Boolean,
    val soilIrrigationActive: Boolean,
    val uvLightActive: Boolean
) {
    val tempStatus: SensorStatus
        get() = when {
            currentTemp < minTemp - 2 || currentTemp > maxTemp + 2 -> SensorStatus.CRITICAL
            currentTemp < minTemp || currentTemp > maxTemp -> SensorStatus.WARNING
            else -> SensorStatus.OPTIMAL
        }

    val airHumidityStatus: SensorStatus
        get() = when {
            currentAirHumidity < minAirHumidity - 10 || currentAirHumidity > maxAirHumidity + 10 -> SensorStatus.CRITICAL
            currentAirHumidity < minAirHumidity || currentAirHumidity > maxAirHumidity -> SensorStatus.WARNING
            else -> SensorStatus.OPTIMAL
        }

    val soilHumidityStatus: SensorStatus
        get() = when {
            currentSoilHumidity < minSoilHumidity - 10 || currentSoilHumidity > maxSoilHumidity + 10 -> SensorStatus.CRITICAL
            currentSoilHumidity < minSoilHumidity || currentSoilHumidity > maxSoilHumidity -> SensorStatus.WARNING
            else -> SensorStatus.OPTIMAL
        }
}

data class ReptileSpeciesInfo(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val category: String, // Tropical, Desértico, Subtropical
    val description: String,
    val recommendedTempMin: Float,
    val recommendedTempMax: Float,
    val recommendedAirHumMin: Float,
    val recommendedAirHumMax: Float,
    val recommendedSoilHumMin: Float,
    val recommendedSoilHumMax: Float,
    val careNotes: String
)

data class EnvironmentalHistoryPoint(
    val timeLabel: String,
    val temp: Float,
    val airHumidity: Float,
    val soilHumidity: Float
)

data class AlertEvent(
    val id: String,
    val timestamp: String,
    val title: String,
    val description: String,
    val isCritical: Boolean
)

object MockReptileData {
    val sampleTerrariums = listOf(
        Terrarium(
            id = "t1",
            name = "Terrario Tropical A1",
            reptileName = "Draco",
            species = "Gecko Crestado (Correlophus ciliatus)",
            currentTemp = 25.4f,
            minTemp = 22.0f,
            maxTemp = 27.0f,
            currentAirHumidity = 74.0f,
            minAirHumidity = 65.0f,
            maxAirHumidity = 85.0f,
            currentSoilHumidity = 58.0f,
            minSoilHumidity = 50.0f,
            maxSoilHumidity = 70.0f,
            heatLampActive = true,
            mistingActive = false,
            soilIrrigationActive = false,
            uvLightActive = true
        ),
        Terrarium(
            id = "t2",
            name = "Terrario Árido B2",
            reptileName = "Ignis",
            species = "Dragón Barbudo (Pogona vitticeps)",
            currentTemp = 34.2f,
            minTemp = 30.0f,
            maxTemp = 38.0f,
            currentAirHumidity = 32.0f,
            minAirHumidity = 25.0f,
            maxAirHumidity = 40.0f,
            currentSoilHumidity = 22.0f,
            minSoilHumidity = 15.0f,
            maxSoilHumidity = 30.0f,
            heatLampActive = true,
            mistingActive = false,
            soilIrrigationActive = false,
            uvLightActive = true
        ),
        Terrarium(
            id = "t3",
            name = "Terrario Selva C3",
            reptileName = "Kaa",
            species = "Pitón Bola (Python regius)",
            currentTemp = 28.1f,
            minTemp = 26.0f,
            maxTemp = 32.0f,
            currentAirHumidity = 52.0f, // Un poco bajo
            minAirHumidity = 55.0f,
            maxAirHumidity = 70.0f,
            currentSoilHumidity = 45.0f,
            minSoilHumidity = 40.0f,
            maxSoilHumidity = 60.0f,
            heatLampActive = true,
            mistingActive = true,
            soilIrrigationActive = false,
            uvLightActive = false
        )
    )

    val speciesCatalog = listOf(
        ReptileSpeciesInfo(
            id = "s1",
            commonName = "Gecko Crestado",
            scientificName = "Correlophus ciliatus",
            category = "Arbóreo Tropical",
            description = "Especie arborícola de Nueva Caledonia. Requiere alta humedad ambiental y sustrato ligeramente húmedo para facilitar la muda.",
            recommendedTempMin = 22.0f,
            recommendedTempMax = 26.5f,
            recommendedAirHumMin = 65.0f,
            recommendedAirHumMax = 85.0f,
            recommendedSoilHumMin = 50.0f,
            recommendedSoilHumMax = 70.0f,
            careNotes = "Pulverizar agua al atardecer. Mantener el sustrato de fibra de coco con humedad constante pero sin encharcar."
        ),
        ReptileSpeciesInfo(
            id = "s2",
            commonName = "Dragón Barbudo",
            scientificName = "Pogona vitticeps",
            category = "Desértico / Árido",
            description = "Reptil de clima árido australiano. Necesita una zona de asoleamiento cálida y baja humedad ambiental y de tierra.",
            recommendedTempMin = 30.0f,
            recommendedTempMax = 38.0f,
            recommendedAirHumMin = 25.0f,
            recommendedAirHumMax = 40.0f,
            recommendedSoilHumMin = 15.0f,
            recommendedSoilHumMax = 30.0f,
            careNotes = "Proporcionar radiación UVB alta (10.0) y gradiente térmico marcado con zona de descanso a 26°C."
        ),
        ReptileSpeciesInfo(
            id = "s3",
            commonName = "Pitón Bola",
            scientificName = "Python regius",
            category = "Subtropical / Sabana",
            description = "Serpiente dócil de África tropical. La humedad de la tierra y del aire son fundamentales para evitar mudas retenidas.",
            recommendedTempMin = 26.0f,
            recommendedTempMax = 32.0f,
            recommendedAirHumMin = 55.0f,
            recommendedAirHumMax = 70.0f,
            recommendedSoilHumMin = 40.0f,
            recommendedSoilHumMax = 60.0f,
            careNotes = "Aumentar humedad de aire al 75% durante el proceso de muda (ecdisis)."
        ),
        ReptileSpeciesInfo(
            id = "s4",
            commonName = "Camaleón Velado",
            scientificName = "Chamaeleo calyptratus",
            category = "Arbóreo Húmedo",
            description = "Camaleón originario del Yémen. Bebe gotas de agua del follaje y requiere buena ventilación junto con humedad controlada.",
            recommendedTempMin = 24.0f,
            recommendedTempMax = 29.0f,
            recommendedAirHumMin = 50.0f,
            recommendedAirHumMax = 75.0f,
            recommendedSoilHumMin = 35.0f,
            recommendedSoilHumMax = 55.0f,
            careNotes = "El nebulizador automático o sistema de goteo en hojas es imprescindible. No beben de recipiente estancado."
        )
    )

    val historyData24h = listOf(
        EnvironmentalHistoryPoint("00:00", 23.5f, 78f, 62f),
        EnvironmentalHistoryPoint("04:00", 22.8f, 80f, 60f),
        EnvironmentalHistoryPoint("08:00", 24.2f, 75f, 58f),
        EnvironmentalHistoryPoint("12:00", 26.1f, 68f, 55f),
        EnvironmentalHistoryPoint("16:00", 26.8f, 65f, 53f),
        EnvironmentalHistoryPoint("20:00", 25.4f, 74f, 58f)
    )

    val recentAlerts = listOf(
        AlertEvent(
            id = "a1",
            timestamp = "Hace 20 min",
            title = "Humedad de aire en descenso",
            description = "La humedad del aire en 'Terrario Selva C3' cayó a 52% (Mínimo: 55%). Nebulizador activado automáticamente.",
            isCritical = false
        ),
        AlertEvent(
            id = "a2",
            timestamp = "Ayer 22:15",
            title = "Temperatura estable",
            description = "Apagado automático de lámpara infrarroja nocturna programado con éxito.",
            isCritical = false
        )
    )
}
