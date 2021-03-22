package com.iti.mad41.taqs.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.mad41.taqs.data.model.Current
import com.iti.mad41.taqs.data.model.DailyItem
import com.iti.mad41.taqs.data.model.HourlyItem
import com.iti.mad41.taqs.data.model.MinutelyItem

class Converters {
    @TypeConverter
    fun convertListOfCurrentToString(current: Current): String? {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun convertStringToCurrent(current: String): Current {
        val type = object : TypeToken<List<DailyItem>>() {}.type
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
    fun convertListOfMinutelyItemsToString(minutelyItems: List<MinutelyItem>): String?{
        return Gson().toJson(minutelyItems)
    }

    @TypeConverter
    fun convertStringToListOfMinutelyItems(minutelyItems: String): List<MinutelyItem> {
        val type = object : TypeToken<List<MinutelyItem>>() {}.type
        return Gson().fromJson(minutelyItems, type)
    }
}