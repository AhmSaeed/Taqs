package com.iti.mad41.taqs.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.mad41.taqs.data.model.WeatherNode

@TypeConverters(Converters::class)
@Database(entities = [WeatherNode::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}