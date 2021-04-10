package com.iti.mad41.taqs.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlertsItem(
        @field:SerializedName("start")
        val start: Int? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("senderName")
        val senderName: String? = null,

        @field:SerializedName("end")
        val end: Int? = null,

        @field:SerializedName("event")
        val event: String? = null
) : Parcelable