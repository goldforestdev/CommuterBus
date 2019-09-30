package com.hppk.sw.hppkcommuterbus.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusLine(
    val id: String = "",
    val name: String = "",
    val busStops: List<BusStop> = listOf()
) : Parcelable

@Parcelize
data class BusStop(
    val name: String = "",
    val time: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
) : Parcelable