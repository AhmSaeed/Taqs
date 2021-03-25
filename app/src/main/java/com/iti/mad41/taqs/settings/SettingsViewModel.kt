package com.iti.mad41.taqs.settings

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import androidx.lifecycle.*
import com.iti.mad41.taqs.data.repo.WeatherRepository
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

    private val _reinitializeActivityEvent = MutableLiveData<String>()
    val reinitializeActivityEvent: LiveData<String> = _reinitializeActivityEvent

    init {
        loadData()
    }

    private fun loadData(){
        _forceUpdate.value = true
    }

    fun setLanguage(lang: String){
        weatherRepository.saveSelectedLanguage(lang)
        reattachBaseContext(lang)
    }

    private fun reattachBaseContext(lang: String){
        _reinitializeActivityEvent.value = lang
    }
}

class SettingsViewModelFactory(val application: Application, val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(application, repository)  as T
    }
}