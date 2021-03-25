package com.iti.mad41.taqs.settings

import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.util.ACCESS_LOCATION_WITH_GPS
import com.iti.mad41.taqs.util.ACCESS_LOCATION_WITH_MAP

enum class AccessLocationType(var value: String) {
    GPS(ACCESS_LOCATION_WITH_GPS),
    Map(ACCESS_LOCATION_WITH_MAP)
}