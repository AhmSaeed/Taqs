package com.iti.mad41.taqs

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.iti.mad41.taqs.data.repo.DefaultIWeatherRepository
import com.iti.mad41.taqs.data.repo.IDefaultWeatherRepository
import com.iti.mad41.taqs.data.source.local.WeatherDatabase
import com.iti.mad41.taqs.data.source.local.WeatherLocaleDataSource
import com.iti.mad41.taqs.data.source.preferences.SharedISharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource

object ServiceLocator {
    private var database: WeatherDatabase? = null
    var repositoryIDefault: IDefaultWeatherRepository? = null

    fun provideRepository(application: Application): IDefaultWeatherRepository{
        return repositoryIDefault ?: createRepository(application)
    }

    private fun createRepository(context: Context): IDefaultWeatherRepository{
        val database = createDataBase(context)
        return DefaultIWeatherRepository(
                WeatherRemoteDataSource(),
                WeatherLocaleDataSource(database.weatherDao()),
                SharedISharedPreferencesDataSource(context)
        )
    }

    private fun createDataBase(context: Context): WeatherDatabase{
        return Room.databaseBuilder(
                context,
                WeatherDatabase::class.java,
                "Taqs.db"
        ).build()
    }
}