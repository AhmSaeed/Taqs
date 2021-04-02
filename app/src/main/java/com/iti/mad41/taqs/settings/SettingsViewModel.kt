package com.iti.mad41.taqs.settings

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.util.ACCESS_LOCATION_WITH_GPS
import com.iti.mad41.taqs.util.Event
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(
        application: Application,
        private val weatherRepository: WeatherRepository
): AndroidViewModel(application) {
    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _accessLocationType = _forceUpdate.map{
        weatherRepository.getSelectedAccessLocationType(AccessLocationType.GPS.value)!!
    } as MutableLiveData<String>
    val accessLocationType: LiveData<String> = _accessLocationType

    private val _language = _forceUpdate.map {
        weatherRepository.getSelectedLanguage(Language.EN.value)!!
    } as MutableLiveData<String>
    val language: LiveData<String> = _language

    private val _temperatureUnit = _forceUpdate.map {
        weatherRepository.getTemperatureUnit(TemperatureUnit.Kelvin.value)!!
    } as MutableLiveData<String>
    val temperatureUnit: LiveData<String> = _temperatureUnit

    private val _windSpeedUnit = _forceUpdate.map {
        weatherRepository.getWindSpeedUnit(WindSpeedUnit.MPS.value)!!
    } as MutableLiveData<String>
    val windSpeedUnit: LiveData<String> = _windSpeedUnit

    private val _requestLocationPermissionEvent = MutableLiveData<Event<Unit>>()
    val requestLocationPermissionEvent: LiveData<Event<Unit>> = _requestLocationPermissionEvent

    private val _openMapEvent = MutableLiveData<Event<Unit>>()
    val openMapEvent: LiveData<Event<Unit>> = _openMapEvent

    private val _reinitializeActivityEvent = MutableLiveData<Event<Unit>>()
    val reinitializeActivityEvent: LiveData<Event<Unit>> = _reinitializeActivityEvent

    private val _snackbarText = MutableLiveData<Int>()
    val snackbarText: LiveData<Int> = _snackbarText

    init {
        loadData()
        weatherRepository.getLocation()
    }

    private fun loadData(){
        _forceUpdate.value = true
    }

    fun onSelectAccessLocationType(type: String){
        if(type.equals(AccessLocationType.Map.value)){
            openMap()
        } else {
            requestLocationPermission()
            saveAccessLocationType(AccessLocationType.GPS.value)
        }
    }

    fun onSelectTemperatureUnit(unit: String){
        saveTemperatureUnit(unit)
        when {
            unit.equals(TemperatureUnit.Celsius.value) -> {
                saveWindSpeedUnit(WindSpeedUnit.MPS.value)
            }
            unit.equals(TemperatureUnit.Fahrenheit.value) -> {
                saveWindSpeedUnit(WindSpeedUnit.MPH.value)
            }
            else -> {
                saveWindSpeedUnit(WindSpeedUnit.MPS.value)
            }
        }
        loadData()
    }

    fun onSelectWindSpeedUnit(unit: String){
        saveWindSpeedUnit(unit)
        when {
            unit.equals(WindSpeedUnit.MPS.value) -> {
                if(_temperatureUnit.value!!.equals(TemperatureUnit.Fahrenheit.value)){
                    saveTemperatureUnit(TemperatureUnit.Kelvin.value)
                }
            }
            else -> {
                saveTemperatureUnit(TemperatureUnit.Fahrenheit.value)
            }
        }
        loadData()
    }

    private fun openMap(){
        _openMapEvent.value = Event(Unit)
    }

    private fun requestLocationPermission(){
        _requestLocationPermissionEvent.value = Event(Unit)
    }

    fun saveAccessLocationType(type: String){
        viewModelScope.launch {
            weatherRepository.saveSelectedAccessLocationType(type)
        }
    }

    fun saveLocation(lat: Double, long: Double, city: String){
        viewModelScope.launch {
            weatherRepository.saveLocation(lat, long, city)
        }
    }

    fun saveLanguage(lang: String){
        viewModelScope.launch {
            weatherRepository.saveSelectedLanguage(lang)
        }
        reattachBaseContext()
    }

    fun saveTemperatureUnit(type: String){
        viewModelScope.launch {
            weatherRepository.saveTemperatureUnit(type)
        }
    }

    fun saveWindSpeedUnit(type: String){
        viewModelScope.launch {
            weatherRepository.saveWindSpeedUnit(type)
        }
    }

    private fun reattachBaseContext(){
        _reinitializeActivityEvent.value = Event(Unit)
    }

    fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = message
    }
}

class SettingsViewModelFactory(val application: Application, val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(application, repository)  as T
    }
}