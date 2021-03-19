package com.iti.mad41.taqs.data.source.remote

import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.util.WEATHER_BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WeatherApi {
    fun getWeatherClient() = Retrofit
        .Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApiServices::class.java)
}

interface WeatherApiServices{
    @GET("data/2.5/onecall")
    suspend fun fetchWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String
    ): Response<WeatherNode>
}