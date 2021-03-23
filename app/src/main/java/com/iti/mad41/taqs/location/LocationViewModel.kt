package com.iti.mad41.taqs.location

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.home.HomeViewModel

class LocationViewModel(context: Context): ViewModel() {
    private val locationLiveData = LocationLiveData(context);
    fun getLocationLiveData() = locationLiveData;
}

class LocationViewModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationViewModel(context)  as T
    }
}