package com.iti.mad41.taqs.data.repo

import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result
import kotlinx.coroutines.flow.Flow

interface IDefaultWeatherRepository {

    suspend fun refreshWeatherData()

    fun observeRemoteWeatherData(): LiveData<Result<WeatherNode>>

    fun observeLocaleWeatherData(): LiveData<WeatherNode>

    suspend fun updateWeatherFromRemoteDataSource()

    suspend fun refreshFavouriteWeatherNode(id: Int)

    suspend fun updateFavouriteWeatherFromRemoteDataSource(id: Int)

    fun observeFavouriteWeatherList(): LiveData<Result<List<WeatherNode>>>

    fun observeFavouriteWeatherNode(id: Int): LiveData<Result<WeatherNode>>

    fun checkForMeasurementUnit(temperatureUnit: String?, windSpeedUnit: String?): String

    suspend fun getPlaceWeatherToAddToFavouriteLocalDataSource(LocationDetails: LocationDetails)

    suspend fun deleteWeatherNodeFromLocalDataSource(id: Int)

    fun searchForWeatherNodeByAddress(searchChars: String): Flow<List<WeatherNode>>

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
}