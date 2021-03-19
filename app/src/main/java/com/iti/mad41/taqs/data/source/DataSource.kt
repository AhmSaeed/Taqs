package com.iti.mad41.taqs.data.source

import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.WeatherNode

interface DataSource {

    fun setWeatherData(data: Result<WeatherNode>)

    fun observeWeatherData(): LiveData<Result<WeatherNode>>

    suspend fun fetchWeatherData(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String
    ): Result<WeatherNode>
}