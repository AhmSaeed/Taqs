package com.iti.mad41.taqs.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "weather")
data class WeatherNode(

	@PrimaryKey(autoGenerate = true)
	@field:SerializedName("id")
	@ColumnInfo(name = "id")
	val id: Int? = null,

	@field:SerializedName("current")
	@ColumnInfo(name = "current")
	val current: Current? = null,

	@field:SerializedName("timezone")
	@ColumnInfo(name = "timezone")
	val timezone: String? = null,

	@field:SerializedName("timezone_offset")
	@ColumnInfo(name = "timezoneOffset")
	val timezoneOffset: Int? = null,

	@field:SerializedName("daily")
	@ColumnInfo(name = "daily")
	val daily: List<DailyItem?>? = null,

	@field:SerializedName("lon")
	@ColumnInfo(name = "lon")
	val lon: Double? = null,

	@field:SerializedName("hourly")
	@ColumnInfo(name = "hourly")
	val hourly: List<HourlyItem?>? = null,

	@field:SerializedName("minutely")
	@ColumnInfo(name = "minutely")
	val minutely: List<MinutelyItem?>? = null,

	@field:SerializedName("lat")
	@ColumnInfo(name = "lat")
	val lat: Double? = null,

	@field:SerializedName("isCurrent")
	@ColumnInfo(name = "isCurrent")
	val isCurrent: Boolean? = null,
) : Parcelable