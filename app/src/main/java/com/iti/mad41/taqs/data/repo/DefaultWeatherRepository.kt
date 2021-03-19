package com.iti.mad41.taqs.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.data.source.Result.Error
import kotlin.Exception

class DefaultWeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val Dispatcher: CoroutineDispatcher = Dispatchers.IO
): WeatherRepository {

    override suspend fun getWeatherData(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String,
        forceUpdate: Boolean
    ): Result<WeatherNode> {
        if(forceUpdate){
            try{
                updateWeatherFromRemoteDataSource(
                    lat,
                    long,
                    units,
                    lang,
                    exclude,
                    appid
                )
            } catch (ex: Exception){
                return Error(ex)
            }
        }
        return Error(Exception("TODO"));//TODO: handle locale Repo here
    }

    override suspend fun refreshWeatherData(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String
    ) {
        updateWeatherFromRemoteDataSource(
            lat,
            long,
            units,
            lang,
            exclude,
            appid
        )
    }

    override fun observeWeatherData(): LiveData<Result<WeatherNode>> {
        return weatherRemoteDataSource.observeWeatherData()
    }

    override suspend fun updateWeatherFromRemoteDataSource(
        lat: Double,
        long: Double,
        units: String,
        lang: String,
        exclude: String,
        appid: String
    ) {
        val remoteWeatherData = weatherRemoteDataSource.fetchWeatherData(
            lat,
            long,
            units,
            lang,
            exclude,
            appid
        )

        if(remoteWeatherData is Success){
            //TODO: handle locale Repo here
            weatherRemoteDataSource.setWeatherData(remoteWeatherData)
        } else if (remoteWeatherData is Error) {
            throw remoteWeatherData.exception
        }
    }
}