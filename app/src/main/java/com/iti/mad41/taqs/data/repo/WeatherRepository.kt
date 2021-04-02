package com.iti.mad41.taqs.data.repo

import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result

interface WeatherRepository {

    suspend fun getWeatherData(
        forceUpdate: Boolean
    ): Result<WeatherNode>

    suspend fun refreshWeatherData()

    fun observeWeatherData(): LiveData<Result<WeatherNode>>

    suspend fun updateWeatherFromRemoteDataSource()

    fun getLocation(): LocationDetails
    suspend fun saveLocation(lat: Double, long: Double, city: String)
    fun getSelectedAccessLocationType(default: String): String?
    suspend fun saveSelectedAccessLocationType(type: String)
    fun getSelectedLanguage(default: String): String?
    suspend fun saveSelectedLanguage(lang: String)
    fun getTemperatureUnit(default: String): String?
    suspend fun saveTemperatureUnit(unit: String)
    fun getWindSpeedUnit(default: String): String?
    suspend fun saveWindSpeedUnit(unit: String)
//    fun saveMeasurementUnit(unit: String)
//    fun getMeasurementUnit(default: String): String?
}