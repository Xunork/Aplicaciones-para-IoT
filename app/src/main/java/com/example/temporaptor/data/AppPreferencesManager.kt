package com.example.temporaptor.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/**
 * SharedPreferences Persistence Manager for TempoRaptor.
 * Handles persistent storage for Onboarding, User Sessions, Notification Choices, and Reptiles/Terrariums data.
 */
object AppPreferencesManager {
    private const val PREFS_NAME = "temporaptor_app_prefs"
    private const val KEY_ONBOARDING_DONE = "onboarding_done"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_LOGGED_USER = "logged_user"
    private const val KEY_NOTIF_ACCEPTED = "notif_accepted"
    private const val KEY_TERRARIUMS_JSON = "terrariums_json"

    fun isOnboardingDone(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ONBOARDING_DONE, false)
    }

    fun setOnboardingDone(context: Context, done: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ONBOARDING_DONE, done)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, loggedIn: Boolean, userName: String = "") {
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn)
        if (userName.isNotBlank()) {
            editor.putString(KEY_LOGGED_USER, userName)
        }
        editor.apply()
    }

    fun getLoggedUser(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LOGGED_USER, "Herpetólogo Pro") ?: "Herpetólogo Pro"
    }

    fun isNotifAccepted(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_NOTIF_ACCEPTED, false)
    }

    fun setNotifAccepted(context: Context, accepted: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_NOTIF_ACCEPTED, accepted)
            .apply()
    }

    /**
     * Persists the list of Terrariums / Reptiles to SharedPreferences as JSON.
     */
    fun saveTerrariums(context: Context, terrariums: List<Terrarium>) {
        val jsonArray = JSONArray()
        for (t in terrariums) {
            val obj = JSONObject().apply {
                put("id", t.id)
                put("name", t.name)
                put("reptileName", t.reptileName)
                put("species", t.species)
                put("currentTemp", t.currentTemp)
                put("minTemp", t.minTemp)
                put("maxTemp", t.maxTemp)
                put("currentAirHumidity", t.currentAirHumidity)
                put("minAirHumidity", t.minAirHumidity)
                put("maxAirHumidity", t.maxAirHumidity)
                put("currentSoilHumidity", t.currentSoilHumidity)
                put("minSoilHumidity", t.minSoilHumidity)
                put("maxSoilHumidity", t.maxSoilHumidity)
                put("heatLampActive", t.heatLampActive)
                put("mistingActive", t.mistingActive)
                put("soilIrrigationActive", t.soilIrrigationActive)
                put("uvLightActive", t.uvLightActive)
            }
            jsonArray.put(obj)
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TERRARIUMS_JSON, jsonArray.toString())
            .apply()
    }

    /**
     * Restores the list of Terrariums / Reptiles from SharedPreferences.
     * Returns null if no saved data exists yet.
     */
    fun loadTerrariums(context: Context): List<Terrarium>? {
        val jsonString = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TERRARIUMS_JSON, null) ?: return null

        return try {
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<Terrarium>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Terrarium(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        name = obj.optString("name", "Terrario"),
                        reptileName = obj.optString("reptileName", "Mascota"),
                        species = obj.optString("species", "Especie"),
                        currentTemp = obj.optDouble("currentTemp", 25.0).toFloat(),
                        minTemp = obj.optDouble("minTemp", 22.0).toFloat(),
                        maxTemp = obj.optDouble("maxTemp", 28.0).toFloat(),
                        currentAirHumidity = obj.optDouble("currentAirHumidity", 65.0).toFloat(),
                        minAirHumidity = obj.optDouble("minAirHumidity", 60.0).toFloat(),
                        maxAirHumidity = obj.optDouble("maxAirHumidity", 80.0).toFloat(),
                        currentSoilHumidity = obj.optDouble("currentSoilHumidity", 55.0).toFloat(),
                        minSoilHumidity = obj.optDouble("minSoilHumidity", 50.0).toFloat(),
                        maxSoilHumidity = obj.optDouble("maxSoilHumidity", 70.0).toFloat(),
                        heatLampActive = obj.optBoolean("heatLampActive", true),
                        mistingActive = obj.optBoolean("mistingActive", false),
                        soilIrrigationActive = obj.optBoolean("soilIrrigationActive", false),
                        uvLightActive = obj.optBoolean("uvLightActive", true),
                    )
                )
            }
            list
        } catch (_: Exception) {
            null
        }
    }
}
