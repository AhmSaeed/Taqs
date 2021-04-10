package com.iti.mad41.taqs.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.mad41.taqs.data.model.*

class Converters {
    @TypeConverter
    fun convertListOfCurrentToString(current: Current): String? {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun convertStringToCurrent(current: String): Current {
        val type = object : TypeToken<Current>() {}.type
        return Gson().fromJson(current, type)
    }

    @TypeConverter
    fun convertListOfDailyItemsToString(dailyItems: List<DailyItem>): String? {
        return Gson().toJson(dailyItems)
    }

    @TypeConverter
    fun convertStringToListOfDailyItems(dailyItems: String): List<DailyItem> {
        val type = object : TypeToken<List<DailyItem>>() {}.type
        return Gson().fromJson(dailyItems, type)
    }

    @TypeConverter
    fun convertListOfHourlyItemsToString(hourlyItems: List<HourlyItem>): String?{
        return Gson().toJson(hourlyItems)
    }

    @TypeConverter
    fun convertStringToListOfHourlyItems(hourlyItems: String): List<HourlyItem> {
        val type = object : TypeToken<List<HourlyItem>>() {}.type
        return Gson().fromJson(hourlyItems, type)
    }

    @TypeConverter
    fun convertListOfAlertItemsToString(alertItems: List<AlertsItem?>?): String?{
        return Gson().toJson(alertItems)
    }

    @TypeConverter
    fun convertStringToListOfAlertItems(alertItems: String): List<AlertsItem?>? {
        val type = object : TypeToken<List<AlertsItem>>() {}.type
        return Gson().fromJson(alertItems, type)
    }
}