package com.iti.mad41.taqs.favouriteDetails

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.repo.IDefaultWeatherRepository
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.settings.TemperatureUnit
import com.iti.mad41.taqs.util.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(
    private val defaultWeatherRepository: IDefaultWeatherRepository
) : ViewModel() {

    private val _weatherNodeId = MutableLiveData<Int>()

    private val _weatherNode = _weatherNodeId.switchMap { weatherNodeId ->
        viewModelScope.launch {
            defaultWeatherRepository.refreshFavouriteWeatherNode(weatherNodeId)
        }
        defaultWeatherRepository.observeFavouriteWeatherNode(weatherNodeId).map {
            handleWeatherResult(it)
        }
    }
    val weatherNode: LiveData<WeatherNode?> = _weatherNode

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun start(weatherNodeId: Int){
        if (_dataLoading.value == true || weatherNodeId == _weatherNodeId.value) {
            return
        }
        _weatherNodeId.value = weatherNodeId
    }

    fun startSearch(){

    }

    fun getTemperatureUnit(): String{
        return when (defaultWeatherRepository.getTemperatureUnit(TemperatureUnit.Kelvin.value)){
            TemperatureUnit.Kelvin.value -> {
                "°K"
            }
            TemperatureUnit.Fahrenheit.value -> {
                "°F"
            }
            else -> {
                "°C"
            }
        }
    }

    private fun handleWeatherResult(weatherNodeResult: Result<WeatherNode>): WeatherNode?{
        return if(weatherNodeResult is Success){
            weatherNodeResult.data
        } else {
            null
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }
}

class FavouriteDetailsViewModelFactory(
    val defaultWeatherRepository: IDefaultWeatherRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouriteDetailsViewModel(defaultWeatherRepository)  as T
    }
}