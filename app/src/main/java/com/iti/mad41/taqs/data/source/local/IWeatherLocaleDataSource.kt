package com.iti.mad41.taqs.data.source.local

import androidx.lifecycle.LiveData
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface IWeatherLocaleDataSource {

    fun observeWeatherNodeByStatus(status: String): LiveData<WeatherNode>

    fun observeWeatherNodeById(id: Int): LiveData<Result<WeatherNode>>

    fun observeWeatherList(status: String): LiveData<Result<List<WeatherNode>>>

    suspend fun getWeatherNodeByStatus(status: String): List<WeatherNode>

    suspend fun getWeatherNodeById(id: Int): Result<WeatherNode>

    suspend fun updateWeatherNode(weatherNode: WeatherNode)

    suspend fun saveWeatherNode(weatherNode: WeatherNode)

    suspend fun deleteWeatherNode(id: Int)

    fun searchForWeatherNodeByAddress(searchChars: String): Flow<List<WeatherNode>>
}