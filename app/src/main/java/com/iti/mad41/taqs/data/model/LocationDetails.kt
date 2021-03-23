package com.iti.mad41.taqs.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDetails (
    @field:SerializedName("longitude")
    val longitude: Double,

    @field:SerializedName("latitude")
    val latitude: Double
) : Parcelable