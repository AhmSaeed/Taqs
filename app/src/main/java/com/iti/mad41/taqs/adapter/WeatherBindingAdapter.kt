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
import com.iti.mad41.taqs.util.IMAGE_BASE_URL
import com.iti.mad41.taqs.util.getDay
import com.iti.mad41.taqs.util.getDayWithDate
import com.iti.mad41.taqs.util.getHour
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

@BindingAdapter("app:dailyItems")
fun setDailyItems(listView: RecyclerView, items: List<DailyItem>?) {
    items?.let {
        (listView.adapter as DailyWeatherAdapter).submitList(items.drop(0))
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
fun loadTemperature(txtView: TextView, temp: Temp){
    val  sharedPreferences = txtView.getContext().getSharedPreferences("", Context.MODE_PRIVATE)
    txtView.setText("${temp.max}/${temp.min} °K")
    //${txtView.resources.getString(R.string.loading_weather_data_error)}
}