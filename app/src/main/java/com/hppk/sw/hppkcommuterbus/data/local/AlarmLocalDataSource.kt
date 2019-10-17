package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import com.google.gson.Gson
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.airbnb.lottie.LottieCompositionFactory.fromJson
import com.google.gson.reflect.TypeToken
import android.R.attr.key



object AlarmLocalDataSource {
    private const val ALARM_ID = "ALARM_ID"


    fun saveAlarm(pref: SharedPreferences, busStopList : List<BusStop>) {
        val busStops = Gson().toJson(busStopList)
        pref.edit().putString(ALARM_ID,busStops).apply()
    }

    fun loadAlarmID (pref: SharedPreferences) : MutableList<BusStop> {
        val emptyList = Gson().toJson(ArrayList<BusStop>())
        return Gson().fromJson(
            pref.getString(ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        )
    }

}