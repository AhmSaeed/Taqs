package com.iti.mad41.taqs.settings

import com.iti.mad41.taqs.util.CELSIUS
import com.iti.mad41.taqs.util.FAHRENHEIT
import com.iti.mad41.taqs.util.KELVIN

enum class WindSpeedUnit(var value: String) {
    MPS(CELSIUS),
    MPH(KELVIN)
}