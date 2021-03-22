package com.iti.mad41.taqs.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.iti.mad41.taqs.data.model.WeatherNode

@Dao
interface WeatherDao {
    @Query("SELECT * FROM Weather WHERE isCurrent = :isCurrent")
    fun observeWeather(isCurrent: Boolean): LiveData<WeatherNode>
}