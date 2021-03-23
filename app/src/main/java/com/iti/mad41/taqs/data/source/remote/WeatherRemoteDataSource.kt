package com.iti.mad41.taqs.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.WeatherDataSource
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.data.source.Result.Error
import kotlin.Exception


class WeatherRemoteDataSource: WeatherDataSource {

    private val observableWeatherData = MutableLiveData<Result<WeatherNode>>()

    override fun setWeatherData(data: Result<WeatherNode>){
        observableWeatherData.value = data
    }

    override fun observeWeatherData(): LiveData<Result<WeatherNode>> {
        return observableWeatherData
    }

    override suspend fun fetchWeatherData(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String
    ): Result<WeatherNode> {
        var result = WeatherApi
            .getWeatherClient()
            .fetchWeatherData(
                lat,
                long,
                units,
                lang,
                exclude,
                appid
            )
        if(result.isSuccessful){
            return Success(result.body()!!)
        }
        return Error(Exception(result.toString()))
    }
}