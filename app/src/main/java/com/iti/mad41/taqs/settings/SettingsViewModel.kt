package com.iti.mad41.taqs.settings

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.util.Event
import java.util.*

class SettingsViewModel(
        application: Application,
        private val weatherRepository: WeatherRepository
): AndroidViewModel(application) {
    private val _forceUpdate = MutableLiveData<Boolean>(false)
    val forceUpdate: LiveData<Boolean> = _forceUpdate

    private val _accessLocationType = _forceUpdate.map{
        weatherRepository.getSelectedAccessLocationType(AccessLocationType.GPS.value)!!
    } as MutableLiveData<String>
    val accessLocationType: LiveData<String> = _accessLocationType

    private val _language = _forceUpdate.map {
        weatherRepository.getSelectedLanguage(Language.EN.value)!!
    } as MutableLiveData<String>
    val language: LiveData<String> = _language

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
    }

    private fun loadData(){
        _forceUpdate.value = true
    }

    fun toggleAccessLocationType(type: String){
        _accessLocationType.value = type
    }

    fun setAccessLocationType(type: String){
        if(type.equals(AccessLocationType.Map.value)){
            openMap()
        } else {
            requestLocationPermission()
        }
    }

    private fun openMap(){
        _openMapEvent.value = Event(Unit)
    }

    private fun requestLocationPermission(){
        _requestLocationPermissionEvent.value = Event(Unit)
    }

    fun setLanguage(lang: String){
        weatherRepository.saveSelectedLanguage(lang)
        reattachBaseContext()
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