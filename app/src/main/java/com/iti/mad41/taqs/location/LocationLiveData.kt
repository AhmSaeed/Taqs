package com.iti.mad41.taqs.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.iti.mad41.taqs.data.model.LocationDetails

class LocationLiveData(val context: Context): LiveData<LocationDetails>() {
    private val MAX_RESULT_VALUE = 1

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        Log.i("requestLocationUpdates", "fusedLocationClient: onActive")
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location: Location? -> location?.also{
                Log.i("requestLocationUpdates", "fusedLocationClient: ${it}")
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationsResult: LocationResult?) {
            super.onLocationResult(locationsResult)

            locationsResult ?: return

            for (location in locationsResult.locations){
                setLocationData(location)
            }
        }
    }

    private fun getLocationGeoCoding(lat: Double, long: Double): String {
        val geoCoder = Geocoder(context)
        val locationsResult = geoCoder.getFromLocation(lat, long, MAX_RESULT_VALUE)

        return if(locationsResult.size != 0) locationsResult[0].adminArea else ""
    }

    private fun setLocationData(location: Location?){
        Log.i("requestLocationUpdates", "fusedLocationClient: setLocationData")
        if(location != null){
            Log.i("requestLocationUpdates", "fusedLocationClient: location != ${location}")
            value = LocationDetails(
                location.longitude,
                location.latitude,
                getLocationGeoCoding(location.latitude, location.longitude)
            )
        }
    }

    companion object{
        val ONE_MINUTE : Long = 60000
        val locationRequest : LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE / 4
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}