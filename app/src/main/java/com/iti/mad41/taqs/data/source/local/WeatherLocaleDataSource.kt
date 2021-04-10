package com.iti.mad41.taqs.data.source.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.data.source.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherLocaleDataSource(
        private val weatherDao: WeatherDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWeatherLocaleDataSource {

    override fun observeWeatherNodeByStatus(status: String): LiveData<WeatherNode> {
        return weatherDao.observeWeatherNodeByStatus(status)
    }

    override fun observeWeatherNodeById(id: Int): LiveData<Result<WeatherNode>> {
        return weatherDao.observeWeatherNodeById(id).map{
            Success(it)
        }
    }

    override fun observeWeatherList(status: String): LiveData<Result<List<WeatherNode>>> {
        return weatherDao.observeWeatherListByStatus(status).map{
            Success(it)
        }
    }

    override suspend fun getWeatherNodeByStatus(status: String): List<WeatherNode> = withContext(ioDispatcher) {
        weatherDao.getWeatherListByStatus(status)
    }

    override suspend fun getWeatherNodeById(id: Int): Result<WeatherNode> = withContext(ioDispatcher) {
        try {
            val weatherNode = weatherDao.getWeatherNodeById(id)

            if(weatherNode != null){
                return@withContext Success(weatherNode)
            } else {
                return@withContext Error(Exception("Data not found!"))
            }
        } catch(e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun updateWeatherNode(weatherNode: WeatherNode) = withContext(ioDispatcher) {
        weatherDao.updateWeatherNode(weatherNode)
    }

    override suspend fun saveWeatherNode(weatherNode: WeatherNode) = withContext(ioDispatcher) {
        weatherDao.insertWeatherNode(weatherNode)
    }

    override suspend fun deleteWeatherNode(id: Int) = withContext(ioDispatcher) {
        weatherDao.deleteWeatherNodeById(id)
    }

    override fun searchForWeatherNodeByAddress(searchChars: String): Flow<List<WeatherNode>> = weatherDao.searchForWeatherNodeByAddress(searchChars)
}