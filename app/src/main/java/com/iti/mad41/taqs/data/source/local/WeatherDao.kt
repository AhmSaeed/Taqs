package com.iti.mad41.taqs.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM Weather WHERE status = :status")
    fun observeWeatherNodeByStatus(status: String): LiveData<WeatherNode>

    @Query("SELECT * FROM WEATHER WHERE status = :status")
    fun observeWeatherListByStatus(status: String): LiveData<List<WeatherNode>>

    @Query("SELECT * FROM WEATHER WHERE id = :id")
    fun observeWeatherNodeById(id: Int): LiveData<WeatherNode>

    @Query("SELECT * from WEATHER WHERE status= :status")
    suspend fun getWeatherListByStatus(status: String): List<WeatherNode>

    @Query("SELECT * FROM WEATHER WHERE id = :id")
    suspend fun getWeatherNodeById(id: Int): WeatherNode

    @Update
    suspend fun updateWeatherNode(weatherNode: WeatherNode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherNode(weatherNode: WeatherNode)

    @Query("DELETE FROM WEATHER WHERE id = :id")
    suspend fun deleteWeatherNodeById(id: Int)

    @Query("SELECT * FROM WEATHER WHERE address LIKE '%' || :searchChars || '%' ")
    fun searchForWeatherNodeByAddress(searchChars: String): Flow<List<WeatherNode>>
}