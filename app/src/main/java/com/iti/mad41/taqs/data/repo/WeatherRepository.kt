package com.iti.mad41.taqs.data.repo

import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result

interface WeatherRepository {

    suspend fun getWeatherData(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String,
        forceUpdate: Boolean
    ): Result<WeatherNode>

    suspend fun refreshWeatherData(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String
    )

    fun observeWeatherData(): LiveData<Result<WeatherNode>>

    suspend fun updateWeatherFromRemoteDataSource(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String
    )

    fun getSelectedAccessLocationType(default: String): String?
    fun saveSelectedAccessLocationType(type: String)
    fun getSelectedLanguage(default: String): String?
    fun saveSelectedLanguage(lang: String)
}