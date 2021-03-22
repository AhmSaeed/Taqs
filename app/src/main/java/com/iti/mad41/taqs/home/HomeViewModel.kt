package com.iti.mad41.taqs.home

import android.util.Log
import androidx.lifecycle.*
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.util.ARABIC
import com.iti.mad41.taqs.util.ENGLISH
import com.iti.mad41.taqs.util.IMPERIAL
import com.iti.mad41.taqs.util.MINUTELY
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _item: LiveData<WeatherNode> = _forceUpdate.switchMap { forceUpdate ->
        if(forceUpdate){
            _dataLoading.value = true
            viewModelScope.launch {
                weatherRepository.refreshWeatherData(
                    lat = 61.5240,
                    long = 105.3188,
                    units = IMPERIAL,
                    lang = ENGLISH,
                    exclude = MINUTELY,
                    appid = "41ffcc244c198f0faa72daa9a9fa68d7"
                )
                _dataLoading.value = false
            }
        }
        weatherRepository.observeWeatherData().switchMap { handleWeatherResult(it) }
    }
    val item: LiveData<WeatherNode> = _item

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    private val _snackBarText = MutableLiveData<Int>()
    val snackBarText: MutableLiveData<Int> = _snackBarText

    init {
        loadWeatherData(true)
    }

    fun loadWeatherData(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun handleWeatherResult(weatherResult: Result<WeatherNode>): LiveData<WeatherNode>{
        val result = MutableLiveData<WeatherNode>()

        if(weatherResult is Success){
            _isDataLoadingError.value = false
            result.value = weatherResult.data!!
        } else {
            showSnackBarMessage(R.string.loading_weather_data_error)
            _isDataLoadingError.value = true
        }
        return result
    }

    private fun showSnackBarMessage(message: Int){
        _snackBarText.value = message
    }
}

class HomeViewModelFactory(val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository)  as T
    }
}