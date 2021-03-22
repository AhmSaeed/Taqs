package com.iti.mad41.taqs.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Snow (

    @field:SerializedName("1h")
    val jsonMember1h: Double? = null

) : Parcelable