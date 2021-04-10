package com.iti.mad41.taqs.location

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocationViewModel(context: Context): ViewModel() {
    private val locationLiveData = LocationLiveData(context)
    fun getLocationLiveData() = locationLiveData
}

class LocationViewModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationViewModel(context)  as T
    }
}