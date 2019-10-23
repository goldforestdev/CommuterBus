package com.hppk.sw.hppkcommuterbus.data.model

import android.os.Parcelable
import com.hppk.sw.hppkcommuterbus.application.language
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusLine(
    val id: String = "",
    val type: Type = Type.GO_OFFICE,
    val name: String = "",
    val nameKr: String = "",
    val busStops: List<BusStop> = listOf()
) : Parcelable {

    val busLineName: String
        get() = if (language != "ko") name else nameKr

}

@Parcelize
data class BusStop(
    val index: Int = 0,
    val type: Type = Type.GO_OFFICE,
    val name: String = "",
    val nameKr: String = "",
    val time: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
) : Parcelable {

    val busStopName: String
        get() = if (language != "ko") name else nameKr

}

enum class Type {
    GO_OFFICE,
    LEAVE_OFFICE
}