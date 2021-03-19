package com.iti.mad41.taqs.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.iti.mad41.taqs.data.model.Temp
import com.iti.mad41.taqs.util.IMAGE_BASE_URL
import com.squareup.picasso.Picasso

@BindingAdapter("android:loadImage")
fun loadImage(imgView: ImageView, url: String){
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

@BindingAdapter("android:loadTemperature")
fun loadTemperature(txtView: TextView, temp: Temp){
    val  sharedPreferences = txtView.getContext().getSharedPreferences("", Context.MODE_PRIVATE)
    txtView.setText("${temp.max} / ${temp.min} K")
}