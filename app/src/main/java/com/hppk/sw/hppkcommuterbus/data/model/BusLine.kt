package com.hppk.sw.hppkcommuterbus.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusLine(
    val id: String = "",
    val type: Type = Type.GO_OFFICE,
    val name: String = "",
    val nameKr: String = "",
    val busStops: List<BusStop> = listOf()
) : Parcelable

@Parcelize
data class BusStop(
    val name: String = "",
    val nameKr: String = "",
    val time: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
) : Parcelable

enum class Type {
    GO_OFFICE,
    LEAVE_OFFICE
}