package com.iti.mad41.taqs.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.data.model.DailyItem
import com.iti.mad41.taqs.data.model.HourlyItem
import com.iti.mad41.taqs.data.model.Temp
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.source.preferences.SHARED_PREF_TEMPERATURE_UNIT
import com.iti.mad41.taqs.settings.TemperatureUnit
import com.iti.mad41.taqs.util.IMAGE_BASE_URL
import com.iti.mad41.taqs.util.getDay
import com.iti.mad41.taqs.util.getDayWithDate
import com.iti.mad41.taqs.util.getHour
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

@BindingAdapter("app:dailyItems")
fun setDailyItems(listView: RecyclerView, items: List<DailyItem>?) {
    items?.let {
        (listView.adapter as DailyWeatherAdapter).submitList(items.drop(1))
    }
}

@BindingAdapter("app:hourlyItems")
fun setHourlyItems(listView: RecyclerView, items: List<HourlyItem>?) {
    items?.let {
        (listView.adapter as HourlyWeatherAdapter).submitList(items)
    }
}

@BindingAdapter("app:favouriteItems")
fun setFavouriteItems(listView: RecyclerView, items: List<WeatherNode>?) {
    items?.let {
        (listView.adapter as FavouritesWeatherAdapter).submitList(items)
    }
}

@BindingAdapter("android:loadImage")
fun loadImage(imgView: ImageView, url: String?){
    url?.let{
        Picasso
            .get()
            .load("${IMAGE_BASE_URL}${url}@2x.png")
            .into(imgView, object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                }

                override fun onError(e: java.lang.Exception?) {
                    //do smth when there is picture loading error
                }
            })
    }
}

@BindingAdapter("android:getDayFromDate")
fun getDay(txtView: TextView, seconds: Long){
    val simpleDateFormat = SimpleDateFormat()
    txtView.text = simpleDateFormat.getDay(seconds)
}

@BindingAdapter("android:getHourFromDate")
fun getHour(txtView: TextView, seconds: Long){
    val simpleDateFormat = SimpleDateFormat()
    txtView.text = simpleDateFormat.getHour(seconds)
}

@BindingAdapter("android:getDayWithDate")
fun getDayWithDate(txtView: TextView, seconds: Long){
    val simpleDateFormat = SimpleDateFormat()
    txtView.text = simpleDateFormat.getDayWithDate(seconds)
}

@BindingAdapter("android:minMaxTemperature")
fun minMaxTemperature(txtView: TextView, temp: Temp){
    val preference: SharedPreferences = txtView.context.applicationContext.getSharedPreferences(
            txtView.context.applicationContext.getString(R.string.taqs_preference_file), Context.MODE_PRIVATE)
    var unit = preference?.getString(SHARED_PREF_TEMPERATURE_UNIT, TemperatureUnit.Kelvin.value)

    txtView.setText("${temp.max}/${temp.min} °${getTemperatureUnit(unit!!)}")
}

@BindingAdapter("android:loadTemperature")
fun loadTemperature(txtView: TextView, temp: String){
    val preference: SharedPreferences = txtView.context.applicationContext.getSharedPreferences(
            txtView.context.applicationContext.getString(R.string.taqs_preference_file), Context.MODE_PRIVATE)
    var unit = preference?.getString(SHARED_PREF_TEMPERATURE_UNIT, TemperatureUnit.Kelvin.value)

    txtView.setText("$temp °${getTemperatureUnit(unit!!)}")
}

fun getTemperatureUnit(unit: String): String{
    return when (unit){
        TemperatureUnit.Kelvin.value -> {
            "K"
        }
        TemperatureUnit.Fahrenheit.value -> {
            "F"
        }
        else -> {
            "C"
        }
    }
}