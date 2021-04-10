package com.iti.mad41.taqs.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result

interface IWeatherRemoteDataSource {
    val observableWeatherData: MutableLiveData<Result<WeatherNode>>
    fun setWeatherData(data: Result<WeatherNode>)
    fun observeWeatherData(): LiveData<Result<WeatherNode>>

    suspend fun getWeatherData(
            lat: Double,
            long: Double,
            units: String,
            lang: String
    ): Result<WeatherNode>
}