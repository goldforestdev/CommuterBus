package com.hppk.sw.hppkcommuterbus.commons

import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import java.util.*

fun getAlarmTime(busStop: BusStop): Long {
    val calendar = Calendar.getInstance()
    val now = calendar.timeInMillis

    busStop.time.split(":").let {
        calendar.set(Calendar.HOUR_OF_DAY, it[0].toInt())
        calendar.set(Calendar.MINUTE, it[1].toInt())
    }

    if (calendar.timeInMillis < now) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return calendar.timeInMillis
}

fun getTomorrowAlarmTime(busStop: BusStop): Long {
    val calendar = Calendar.getInstance()

    busStop.time.split(":").let {
        calendar.set(Calendar.HOUR_OF_DAY, it[0].toInt())
        calendar.set(Calendar.MINUTE, it[1].toInt())
    }

    calendar.add(Calendar.DAY_OF_MONTH, 1)

    return calendar.timeInMillis
}