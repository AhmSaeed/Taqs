package com.iti.mad41.taqs.map

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.repo.WeatherRepository
import kotlinx.coroutines.launch

class MapsViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _snackbarText = MutableLiveData<Int>()
    val snackbarText: LiveData<Int> = _snackbarText

    private val _locationDetails = MutableLiveData<LocationDetails>()
    val locationDetails: LiveData<LocationDetails> = _locationDetails

    fun setLocationDetails(lat: Double, long: Double, city: String){
        _locationDetails.value = LocationDetails(lat, long, city)
    }

    fun saveLocationIncludingType(type: String){
        saveLocation()
        saveAccessLocationType(type)
    }

    fun saveLocation(){
        _locationDetails.value?.latitude ?: return
        _locationDetails.value?.longitude ?: return
        _locationDetails.value?.address ?: return
        viewModelScope.launch {
            weatherRepository.saveLocation(
                    _locationDetails.value?.latitude!!,
                    _locationDetails.value?.longitude!!,
                    _locationDetails.value?.address!!
            )
        }
    }

    fun saveAccessLocationType(type: String){
        viewModelScope.launch {
            weatherRepository.saveSelectedAccessLocationType(type)
        }
    }

    fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = message
    }
}

class MapsViewModelFactory(
    val repository: WeatherRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapsViewModel(repository) as T
    }
}