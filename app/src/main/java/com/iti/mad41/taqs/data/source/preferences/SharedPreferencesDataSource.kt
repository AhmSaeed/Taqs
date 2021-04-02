package com.iti.mad41.taqs.data.source.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.util.ENGLISH

const val SHARED_PREF_ACCESS_LOCATION_TYPE_KEY  = "sharedPreferencesAccessLocationType"
const val SHARED_PREF_LANGUAGE_KEY  = "sharedPreferencesLanguage"
const val SHARED_PREF_LATITUDE_KEY  = "sharedPreferencesLatitude"
const val SHARED_PREF_LONGITUDE_KEY  = "sharedPreferencesLongitude"
const val SHARED_PREF_CITY_KEY  = "sharedPreferencesCity"
const val SHARED_PREF_MEASUREMENT_UNIT = "sharedPreferencesMeasurementUnit"
const val SHARED_PREF_TEMPERATURE_UNIT = "sharedPreferencesTemperatureUnit"
const val SHARED_PREF_WIND_SPEED_UNIT = "sharedPreferencesWindSpeedUnit"

class SharedPreferencesDataSource(
    context: Context
): PreferencesDataSource {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences?
        get() = appContext?.getSharedPreferences(
                    appContext.getString(R.string.taqs_preference_file), Context.MODE_PRIVATE)

    override fun saveLocation(lat: Double, long: Double, city: String){
        preference ?: return
        with(preference!!.edit()){
            putFloat(SHARED_PREF_LATITUDE_KEY, lat.toFloat())
            putFloat(SHARED_PREF_LONGITUDE_KEY, long.toFloat())
            putString(SHARED_PREF_CITY_KEY, city)
            apply()
        }
    }

    override fun getLocation(): LocationDetails {
        var lat = preference?.getFloat(SHARED_PREF_LATITUDE_KEY, 0.0F)?.toDouble()
        var long = preference?.getFloat(SHARED_PREF_LONGITUDE_KEY, 0.0F)?.toDouble()
        var address = preference?.getString(SHARED_PREF_CITY_KEY, "")
        return LocationDetails(
                latitude = lat!!,
                longitude = long!!,
                address = address!!
        )
    }

    override fun saveSelectedAccessLocationType(type: String){
        preference ?: return
        with(preference!!.edit()){
            putString(SHARED_PREF_ACCESS_LOCATION_TYPE_KEY, type)
            apply()
        }
    }

    override fun getSelectedAccessLocationType(default: String): String? {
        return preference?.getString(SHARED_PREF_ACCESS_LOCATION_TYPE_KEY, default)
    }

    override fun saveSelectedLanguage(lang: String) {
        preference ?: return
        with(preference!!.edit()){
            putString(SHARED_PREF_LANGUAGE_KEY, lang)
            apply()
        }
    }

    override fun getSelectedLanguage(default: String): String? {
        return preference?.getString(SHARED_PREF_LANGUAGE_KEY, default)
    }

    override fun saveTemperatureUnit(unit: String){
        preference ?: return
        with(preference!!.edit()){
            putString(SHARED_PREF_TEMPERATURE_UNIT, unit)
            apply()
        }
    }

    override fun getTemperatureUnit(default: String): String? {
        return preference?.getString(SHARED_PREF_TEMPERATURE_UNIT, default)
    }

    override fun saveWindSpeedUnit(unit: String){
        preference ?: return
        with(preference!!.edit()){
            putString(SHARED_PREF_WIND_SPEED_UNIT, unit)
            apply()
        }
    }

    override fun getWindSpeedUnit(default: String): String? {
        return preference?.getString(SHARED_PREF_WIND_SPEED_UNIT, default)
    }


}