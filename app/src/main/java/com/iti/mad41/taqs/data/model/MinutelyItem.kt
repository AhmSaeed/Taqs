package com.iti.mad41.taqs.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MinutelyItem(

	@field:SerializedName("dt")
	val dt: Int? = null,

	@field:SerializedName("precipitation")
	val precipitation: Double? = null
) : Parcelable