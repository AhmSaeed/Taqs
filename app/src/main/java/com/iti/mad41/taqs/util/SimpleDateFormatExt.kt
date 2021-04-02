package com.iti.mad41.taqs.util

import java.text.SimpleDateFormat

fun SimpleDateFormat.getDay(seconds: Long): String? {
    val simpleDateFormat = SimpleDateFormat("EEEE")
    val dateString = simpleDateFormat.format(seconds * 1000)
    return dateString
}

fun SimpleDateFormat.getHour(seconds: Long): String? {
    val simpleDateFormat = SimpleDateFormat("h a")
    val dateString = simpleDateFormat.format(seconds * 1000)
    return dateString
}

fun SimpleDateFormat.getDayWithDate(seconds: Long): String? {
    val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM")
    val dateString = simpleDateFormat.format(seconds * 1000)
    return dateString
}