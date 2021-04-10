package com.iti.mad41.taqs.map

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.repo.IDefaultWeatherRepository
import com.iti.mad41.taqs.util.Event
import kotlinx.coroutines.launch

class SharedMapsViewModel : ViewModel() {
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _locationDetails = MutableLiveData<Event<LocationDetails>>()
    val locationDetails: LiveData<Event<LocationDetails>> = _locationDetails

    private var _isUpdate = false

    fun setLocationDetails(lat: Double, long: Double, city: String){
        _locationDetails.value = Event(LocationDetails(long, lat, city))
    }

    fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

    fun hasUpdate(isUpdate: Boolean){
        _isUpdate = isUpdate
    }

    fun isUpdate(): Boolean {
        return _isUpdate
    }
}