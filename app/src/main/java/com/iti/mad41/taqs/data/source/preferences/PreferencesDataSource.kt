package com.iti.mad41.taqs.data.source.preferences

import com.iti.mad41.taqs.data.model.LocationDetails

interface PreferencesDataSource {
    fun saveLocation(lat: Double, long: Double, city: String)
    fun getLocation(): LocationDetails

    fun saveSelectedAccessLocationType(type: String)
    fun getSelectedAccessLocationType(default: String): String?

    fun saveSelectedLanguage(lang: String)
    fun getSelectedLanguage(default: String): String?

    fun saveTemperatureUnit(unit: String)
    fun getTemperatureUnit(default: String): String?

    fun saveWindSpeedUnit(unit: String)
    fun getWindSpeedUnit(default: String): String?
}