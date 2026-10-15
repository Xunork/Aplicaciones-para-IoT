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
    val category: String, // Desértico, Arbóreo Tropical, Subtropical, Terrestre, Semiacuático
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
            species = "Gecko crestado (Correlophus ciliatus)",
            currentTemp = 25.4f,
            minTemp = 21.0f,
            maxTemp = 26.5f,
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
            species = "Dragón barbudo (Pogona vitticeps)",
            currentTemp = 34.2f,
            minTemp = 30.0f,
            maxTemp = 38.0f,
            currentAirHumidity = 32.0f,
            minAirHumidity = 25.0f,
            maxAirHumidity = 40.0f,
            currentSoilHumidity = 22.0f,
            minSoilHumidity = 10.0f,
            maxSoilHumidity = 25.0f,
            heatLampActive = true,
            mistingActive = false,
            soilIrrigationActive = false,
            uvLightActive = true
        ),
        Terrarium(
            id = "t3",
            name = "Terrario Selva C3",
            reptileName = "Kaa",
            species = "Pitón bola (Python regius)",
            currentTemp = 28.1f,
            minTemp = 26.0f,
            maxTemp = 32.0f,
            currentAirHumidity = 52.0f,
            minAirHumidity = 55.0f,
            maxAirHumidity = 75.0f,
            currentSoilHumidity = 48.0f,
            minSoilHumidity = 45.0f,
            maxSoilHumidity = 65.0f,
            heatLampActive = true,
            mistingActive = true,
            soilIrrigationActive = false,
            uvLightActive = false
        )
    )

    val speciesCatalog = listOf(
        ReptileSpeciesInfo(
            id = "s1",
            commonName = "Gecko leopardo",
            scientificName = "Eublepharis macularius",
            category = "Desértico / Árido",
            description = "Gradiente térmico: Requiere calor por contacto inferior o infrarrojo focalizado en un tercio del terrario.\nManejo del sustrato: El sustrato general debe estar seco para evitar infecciones pulmonares. Se debe incluir un refugio con musgo sphagnum o fibra de coco húmeda (60% - 70%) para facilitar la muda de piel.",
            recommendedTempMin = 28.0f,
            recommendedTempMax = 32.0f,
            recommendedAirHumMin = 30.0f,
            recommendedAirHumMax = 40.0f,
            recommendedSoilHumMin = 15.0f,
            recommendedSoilHumMax = 30.0f,
            careNotes = "Sustrato general seco; mantener refugio con musgo o fibra de coco húmeda al 60%-70% para ecdisis sin problemas."
        ),
        ReptileSpeciesInfo(
            id = "s2",
            commonName = "Dragón barbudo",
            scientificName = "Pogona vitticeps",
            category = "Desértico / Árido",
            description = "Gradiente térmico: Requiere radiación combinada de calor y luz UVB (10.0 a 14.0 o T5 HO) exclusivamente diurna.\nManejo del sustrato: El exceso de humedad ambiental o en el sustrato desencadena dermatitis micótica y problemas respiratorios graves; la tierra o arena desértica debe mantenerse sin humedad residual.",
            recommendedTempMin = 30.0f,
            recommendedTempMax = 38.0f,
            recommendedAirHumMin = 25.0f,
            recommendedAirHumMax = 40.0f,
            recommendedSoilHumMin = 10.0f,
            recommendedSoilHumMax = 25.0f,
            careNotes = "Mantener radiación UVB alta y sustrato totalmente seco para prevenir dermatitis micótica y afecciones respiratorias."
        ),
        ReptileSpeciesInfo(
            id = "s3",
            commonName = "Serpiente del maíz",
            scientificName = "Pantherophis guttatus",
            category = "Terrestre / Templado",
            description = "Gradiente térmico: Mantener una manta o placa térmica controlada por termostato en la base o un proyector cerámico.\nManejo del sustrato: Sustratos como viruta de álamo (aspen) o corteza fina deben estar secos; la retención de agua encharcada pudre las escamas ventrales (scale rot).",
            recommendedTempMin = 24.0f,
            recommendedTempMax = 30.0f,
            recommendedAirHumMin = 40.0f,
            recommendedAirHumMax = 55.0f,
            recommendedSoilHumMin = 20.0f,
            recommendedSoilHumMax = 35.0f,
            careNotes = "Evitar encharcamientos en sustrato aspen o corteza fina para evitar pudrición de escamas ventrales (scale rot)."
        ),
        ReptileSpeciesInfo(
            id = "s4",
            commonName = "Pitón bola",
            scientificName = "Python regius",
            category = "Subtropical / Sabana",
            description = "Gradiente térmico: Requiere fuentes que no resequen el aire en exceso para sostener la humedad sin fluctuaciones drásticas.\nManejo del sustrato: La fibra de coco, corteza de abeto o chips de ciprés deben retener humedad sin condensar agua líquida permanente en el fondo.",
            recommendedTempMin = 26.0f,
            recommendedTempMax = 32.0f,
            recommendedAirHumMin = 55.0f,
            recommendedAirHumMax = 75.0f,
            recommendedSoilHumMin = 45.0f,
            recommendedSoilHumMax = 65.0f,
            careNotes = "Utilizar fibra de coco o corteza de abeto que retenga humedad ambiental sin acumular condensación estancada."
        ),
        ReptileSpeciesInfo(
            id = "s5",
            commonName = "Gecko crestado",
            scientificName = "Correlophus ciliatus",
            category = "Arbóreo Tropical",
            description = "Gradiente térmico: No tolera temperaturas superiores a 27°C o 28°C; los golpes de calor son letales con rapidez en esta especie.\nManejo del sustrato: Requiere sustrato vegetal para terrarios tropicales (tierra orgánica, fibra de coco y hojarasca) capaz de drenar y mantener humedad continua para plantas vivas.",
            recommendedTempMin = 21.0f,
            recommendedTempMax = 26.5f,
            recommendedAirHumMin = 65.0f,
            recommendedAirHumMax = 85.0f,
            recommendedSoilHumMin = 50.0f,
            recommendedSoilHumMax = 70.0f,
            careNotes = "Evitar temperaturas superiores a 27°C-28°C (golpes de calor letales). Mantener sustrato vegetal tropical húmedo y drenado."
        ),
        ReptileSpeciesInfo(
            id = "s6",
            commonName = "Tortuga rusa",
            scientificName = "Testudo horsfieldii",
            category = "Desértico / Árido",
            description = "Gradiente térmico: Necesita un punto de calor visible potente y UVB de alta intensidad para procesar el calcio de su dieta herbívora.\nManejo del sustrato: Una mezcla de tierra con arena de al menos 10 a 15 cm de profundidad le permite cavar; la superficie debe mantenerse seca y bien ventilada.",
            recommendedTempMin = 22.0f,
            recommendedTempMax = 32.0f,
            recommendedAirHumMin = 30.0f,
            recommendedAirHumMax = 50.0f,
            recommendedSoilHumMin = 20.0f,
            recommendedSoilHumMax = 40.0f,
            careNotes = "Proveer 10-15 cm de sustrato de tierra y arena para excavación. Mantener la superficie bien ventilada y seca."
        ),
        ReptileSpeciesInfo(
            id = "s7",
            commonName = "Tortuga acuática / de orejas rojas",
            scientificName = "Trachemys scripta elegans",
            category = "Semiacuático",
            description = "Gradiente térmico: El agua debe contar con calentador sumergible con protección plástica, y la plataforma exterior seca debe mantenerse a más de 30°C para incentivarlas a salir del agua a regular su metabolismo.\nManejo del sustrato: Al desarrollarse dentro de agua filtrada, el concepto de humedad de suelo aplica únicamente si se habilita un sector con tierra para desove en hembras maduras.",
            recommendedTempMin = 25.0f,
            recommendedTempMax = 31.0f,
            recommendedAirHumMin = 50.0f,
            recommendedAirHumMax = 70.0f,
            recommendedSoilHumMin = 40.0f,
            recommendedSoilHumMax = 80.0f,
            careNotes = "Plataforma de descanso seca a >30°C y agua templada con calentador protegido. Habilitar zona de tierra si hay hembras maduras."
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
