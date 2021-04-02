package com.iti.mad41.taqs.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.data.source.Result.Error
import com.iti.mad41.taqs.data.source.preferences.PreferencesDataSource
import com.iti.mad41.taqs.data.source.preferences.SHARED_PREF_TEMPERATURE_UNIT
import com.iti.mad41.taqs.data.source.preferences.SHARED_PREF_WIND_SPEED_UNIT
import com.iti.mad41.taqs.data.source.preferences.SharedPreferencesDataSource
import com.iti.mad41.taqs.util.*
import kotlin.Exception

class DefaultWeatherRepository(
        private val weatherRemoteDataSource: WeatherRemoteDataSource,
        private val preferencesDataSource: PreferencesDataSource,
        private val Dispatcher: CoroutineDispatcher = Dispatchers.IO
): WeatherRepository {

    override suspend fun getWeatherData(
        forceUpdate: Boolean
    ): Result<WeatherNode> {
        if(forceUpdate){
            try{
                updateWeatherFromRemoteDataSource()
            } catch (ex: Exception){
                return Error(ex)
            }
        }
        return Error(Exception("TODO"));//TODO: handle locale Repo here
    }

    override suspend fun refreshWeatherData() {
        updateWeatherFromRemoteDataSource()
    }

    override fun observeWeatherData(): LiveData<Result<WeatherNode>> {
        return weatherRemoteDataSource.observeWeatherData()
    }

    override suspend fun updateWeatherFromRemoteDataSource() {
        val locationDetails = preferencesDataSource.getLocation()
        val language = preferencesDataSource.getSelectedLanguage(ENGLISH)
        val temperatureUnit = preferencesDataSource.getTemperatureUnit(KELVIN)
        val windSpeedUnit = preferencesDataSource.getWindSpeedUnit(METER_PER_SECOND)

        val measurementUnit: String = if(temperatureUnit?.equals(FAHRENHEIT) == true && windSpeedUnit?.equals(MILE_PER_HOUR) == true){
            IMPERIAL
        } else if(temperatureUnit?.equals(KELVIN) == true && windSpeedUnit?.equals(METER_PER_SECOND) == true){
            STANDARD
        } else if(temperatureUnit?.equals(CELSIUS) == true && windSpeedUnit?.equals(METER_PER_SECOND) == true){
            METRIC
        } else {
            STANDARD
        }
        val remoteWeatherData = weatherRemoteDataSource.fetchWeatherData(
            lat = locationDetails.latitude,
            long = locationDetails.longitude,
            units = measurementUnit,
            lang = language!!
        )

        weatherRemoteDataSource.setWeatherData(remoteWeatherData)
    }

    override suspend fun saveLocation(lat: Double, long: Double, city: String) {
        preferencesDataSource.saveLocation(lat, long, city)
        updateWeatherFromRemoteDataSource()
    }
    override fun getLocation(): LocationDetails = preferencesDataSource.getLocation()

    override suspend fun saveSelectedAccessLocationType(type: String) {
        preferencesDataSource.saveSelectedAccessLocationType(type)
        updateWeatherFromRemoteDataSource()
    }
    override fun getSelectedAccessLocationType(default: String): String? = preferencesDataSource.getSelectedAccessLocationType(default)

    override suspend fun saveSelectedLanguage(lang: String) {
        preferencesDataSource.saveSelectedLanguage(lang)
        updateWeatherFromRemoteDataSource()
    }
    override fun getSelectedLanguage(default: String): String? = preferencesDataSource.getSelectedLanguage(default)

    override suspend fun saveTemperatureUnit(unit: String) {
        preferencesDataSource.saveTemperatureUnit(unit)
        updateWeatherFromRemoteDataSource()
    }
    override fun getTemperatureUnit(default: String): String? = preferencesDataSource.getTemperatureUnit(default)

    override suspend fun saveWindSpeedUnit(unit: String) {
        preferencesDataSource.saveWindSpeedUnit(unit)
        updateWeatherFromRemoteDataSource()
    }
    override fun getWindSpeedUnit(default: String): String? = preferencesDataSource.getWindSpeedUnit(default)

//    override fun saveMeasurementUnit(unit: String) = preferencesDataSource.saveMeasurementUnit(unit)
//    override fun getMeasurementUnit(default: String): String? = preferencesDataSource.getMeasurementUnit(default)
}