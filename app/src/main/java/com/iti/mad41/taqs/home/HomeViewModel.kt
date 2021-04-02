package com.iti.mad41.taqs.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.TaqsApplication
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.settings.AccessLocationType
import com.iti.mad41.taqs.util.ARABIC
import com.iti.mad41.taqs.util.ENGLISH
import com.iti.mad41.taqs.util.IMPERIAL
import com.iti.mad41.taqs.util.MINUTELY
import kotlinx.coroutines.launch

class HomeViewModel(
        application: Application,
        private val weatherRepository: WeatherRepository
) : AndroidViewModel(application) {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _item: LiveData<WeatherNode> = _forceUpdate.switchMap { forceUpdate ->
        if(forceUpdate){
            if(hasInternetConnection()){
                _dataLoading.value = true
                viewModelScope.launch {
                    weatherRepository.refreshWeatherData()
                    _dataLoading.value = false
                }
            } else {
                showSnackBarMessage(R.string.no_internet_connection)
                _isDataLoadingError.value = true
            }
        }
        weatherRepository.observeWeatherData().switchMap { handleWeatherResult(it) }
    }
    val item: LiveData<WeatherNode> = _item

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    private val _accessLocationType = _forceUpdate.map{
        weatherRepository.getSelectedAccessLocationType(AccessLocationType.GPS.value)!!
    } as MutableLiveData<String>
    val accessLocationType: LiveData<String> = _accessLocationType

    private val _locationDetails = _forceUpdate.map{
        weatherRepository.getLocation()
    } as MutableLiveData<LocationDetails>
    val locationDetails: LiveData<LocationDetails> = _locationDetails

    private val _snackbarText = MutableLiveData<Int>()
    val snackbarText: LiveData<Int> = _snackbarText

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

    fun saveLocation(lat: Double, long: Double, city: String){
        viewModelScope.launch {
            weatherRepository.saveLocation(lat, long, city)
        }
    }

    private fun showSnackBarMessage(message: Int){
        _snackbarText.value = message
    }

    private fun hasInternetConnection(): Boolean{
        val connectionManager = getApplication<TaqsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectionManager.activeNetwork ?: return false
            val capabilities = connectionManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectionManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}

class HomeViewModelFactory(
    val application: Application,
    val repository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(application, repository)  as T
    }
}