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
import com.iti.mad41.taqs.data.source.local.WeatherLocaleDataSource
import com.iti.mad41.taqs.data.source.preferences.ISharedPreferencesDataSource
import com.iti.mad41.taqs.util.*
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class DefaultIWeatherRepository(
        private val weatherRemoteDataSource: WeatherRemoteDataSource,
        private val weatherLocaleDataSource: WeatherLocaleDataSource,
        private val ISharedPreferencesDataSource: ISharedPreferencesDataSource,
        private val Dispatcher: CoroutineDispatcher = Dispatchers.IO
): IDefaultWeatherRepository {

    override suspend fun refreshWeatherData() {
        updateWeatherFromRemoteDataSource()
    }

    override fun observeRemoteWeatherData(): LiveData<Result<WeatherNode>> {
        return weatherRemoteDataSource.observeWeatherData()
    }

    override fun observeLocaleWeatherData(): LiveData<WeatherNode> {
        return weatherLocaleDataSource.observeWeatherNodeByStatus(WEATHER_STATUS_CURRENT)
    }

    override suspend fun updateWeatherFromRemoteDataSource() {
        val locationDetails = ISharedPreferencesDataSource.getLocation()
        val language = ISharedPreferencesDataSource.getSelectedLanguage(ENGLISH)
        val temperatureUnit = ISharedPreferencesDataSource.getTemperatureUnit(KELVIN)
        val windSpeedUnit = ISharedPreferencesDataSource.getWindSpeedUnit(METER_PER_SECOND)
        val measurementUnit = checkForMeasurementUnit(temperatureUnit, windSpeedUnit)

        val remoteWeatherData = weatherRemoteDataSource.getWeatherData(
            lat = locationDetails.latitude,
            long = locationDetails.longitude,
            units = measurementUnit,
            lang = language!!
        )

        if(remoteWeatherData is Success){
            remoteWeatherData.data.address = locationDetails.address
            remoteWeatherData.data.status = WEATHER_STATUS_CURRENT
            addCurrentWeatherNode(remoteWeatherData.data)
        } else if (remoteWeatherData is Error) {
            throw remoteWeatherData.exception
        }

        weatherRemoteDataSource.setWeatherData(remoteWeatherData)
    }

    override suspend fun refreshFavouriteWeatherNode(id: Int) {
        updateFavouriteWeatherFromRemoteDataSource(id)
    }

    override suspend fun updateFavouriteWeatherFromRemoteDataSource(id: Int) {
        val localeWeatherNode = weatherLocaleDataSource.getWeatherNodeById(id)

        if(localeWeatherNode is Success) {
            val language = ISharedPreferencesDataSource.getSelectedLanguage(ENGLISH)
            val temperatureUnit = ISharedPreferencesDataSource.getTemperatureUnit(KELVIN)
            val windSpeedUnit = ISharedPreferencesDataSource.getWindSpeedUnit(METER_PER_SECOND)
            val measurementUnit = checkForMeasurementUnit(temperatureUnit, windSpeedUnit)

            val remoteWeatherData = weatherRemoteDataSource.getWeatherData(
                    lat = localeWeatherNode.data.lat ?: 0.0,
                    long = localeWeatherNode.data.lon ?: 0.0,
                    units = measurementUnit,
                    lang = language!!
            )

            if(remoteWeatherData is Success){
                remoteWeatherData.data.id = localeWeatherNode.data.id
                remoteWeatherData.data.address = localeWeatherNode.data.address
                remoteWeatherData.data.status = WEATHER_STATUS_FAVOURITES
                weatherLocaleDataSource.updateWeatherNode(remoteWeatherData.data)
            }
        }
    }

    override fun observeFavouriteWeatherList(): LiveData<Result<List<WeatherNode>>> {
        return weatherLocaleDataSource.observeWeatherList(WEATHER_STATUS_FAVOURITES)
    }

    override fun observeFavouriteWeatherNode(id: Int): LiveData<Result<WeatherNode>> {
        return weatherLocaleDataSource.observeWeatherNodeById(id)
    }

    override fun checkForMeasurementUnit(temperatureUnit: String?, windSpeedUnit: String?): String{
        return when {
            temperatureUnit?.equals(FAHRENHEIT) == true -> {
                IMPERIAL
            }
            temperatureUnit?.equals(KELVIN) == true -> {
                STANDARD
            }
            temperatureUnit?.equals(CELSIUS) == true -> {
                METRIC
            }
            else -> {
                STANDARD
            }
        }
    }

    override suspend fun getPlaceWeatherToAddToFavouriteLocalDataSource(locationDetails: LocationDetails) {
        val language = ISharedPreferencesDataSource.getSelectedLanguage(ENGLISH)
        val temperatureUnit = ISharedPreferencesDataSource.getTemperatureUnit(KELVIN)
        val windSpeedUnit = ISharedPreferencesDataSource.getWindSpeedUnit(METER_PER_SECOND)
        val measurementUnit = checkForMeasurementUnit(temperatureUnit, windSpeedUnit)

        val remoteWeatherNode =  weatherRemoteDataSource.getWeatherData(
                lat = locationDetails.latitude,
                long = locationDetails.longitude,
                units = measurementUnit,
                lang = language!!
        )

        if(remoteWeatherNode is Success){
            remoteWeatherNode.data.address = locationDetails.address
            remoteWeatherNode.data.status = WEATHER_STATUS_FAVOURITES
            weatherLocaleDataSource.saveWeatherNode(remoteWeatherNode.data)
            weatherLocaleDataSource.observeWeatherList(WEATHER_STATUS_FAVOURITES)
        }
    }

    override suspend fun deleteWeatherNodeFromLocalDataSource(id: Int){
        weatherLocaleDataSource.deleteWeatherNode(id)
    }

    suspend fun addCurrentWeatherNode(weatherNode: WeatherNode){
        var remoteWeatherList = weatherLocaleDataSource.getWeatherNodeByStatus(WEATHER_STATUS_CURRENT)

        if(remoteWeatherList.isEmpty()){
            weatherLocaleDataSource.saveWeatherNode(weatherNode)
        } else {
            weatherLocaleDataSource.updateWeatherNode(weatherNode)
        }
    }

    override fun searchForWeatherNodeByAddress(searchChars: String): Flow<List<WeatherNode>> {
        return weatherLocaleDataSource.searchForWeatherNodeByAddress(searchChars)
    }

    override suspend fun saveLocation(lat: Double, long: Double, city: String) {
        ISharedPreferencesDataSource.saveLocation(lat, long, city)
        updateWeatherFromRemoteDataSource()
    }
    override fun getLocation(): LocationDetails = ISharedPreferencesDataSource.getLocation()

    override suspend fun saveSelectedAccessLocationType(type: String) {
        ISharedPreferencesDataSource.saveSelectedAccessLocationType(type)
        updateWeatherFromRemoteDataSource()
    }
    override fun getSelectedAccessLocationType(default: String): String? = ISharedPreferencesDataSource.getSelectedAccessLocationType(default)

    override suspend fun saveSelectedLanguage(lang: String) {
        ISharedPreferencesDataSource.saveSelectedLanguage(lang)
        updateWeatherFromRemoteDataSource()
    }
    override fun getSelectedLanguage(default: String): String? = ISharedPreferencesDataSource.getSelectedLanguage(default)

    override suspend fun saveTemperatureUnit(unit: String) {
        ISharedPreferencesDataSource.saveTemperatureUnit(unit)
        updateWeatherFromRemoteDataSource()
    }
    override fun getTemperatureUnit(default: String): String? = ISharedPreferencesDataSource.getTemperatureUnit(default)

    override suspend fun saveWindSpeedUnit(unit: String) {
        ISharedPreferencesDataSource.saveWindSpeedUnit(unit)
        updateWeatherFromRemoteDataSource()
    }
    override fun getWindSpeedUnit(default: String): String? = ISharedPreferencesDataSource.getWindSpeedUnit(default)
}