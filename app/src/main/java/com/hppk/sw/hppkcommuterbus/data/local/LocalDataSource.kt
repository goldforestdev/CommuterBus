package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

object LocalDataSource {
    private const val FAVORITES_ID = "FAVORITES_ID"
    private const val TIME_ALARM_ID = "TIME_ALARM_ID"
    private const val LOCATION_ALARM_ID = "LOCATION_ALARM_ID"
    private val gSon: Gson by lazy { Gson() }


    fun saveFavoriteID(pref: SharedPreferences,favoritesDataList : List<BusLine>) {
        val busLines = gSon.toJson(favoritesDataList)
        pref.edit().putString(FAVORITES_ID,busLines).apply()

    }

    fun loadFavoriteID (pref: SharedPreferences) : MutableList<BusLine> {
        val emptyList = gSon.toJson(ArrayList<BusLine>())
        return gSon.fromJson(
            pref.getString(FAVORITES_ID, emptyList),
            object : TypeToken<ArrayList<BusLine>>() {
            }.type
        )
    }

    fun saveTimeAlarm(pref: SharedPreferences, busStopList : List<BusStop>) {
        val busStops = gSon.toJson(busStopList)
        pref.edit().putString(TIME_ALARM_ID,busStops).apply()
    }

    fun loadTimeAlarmID (pref: SharedPreferences) : MutableList<BusStop> {
        val emptyList = gSon.toJson(ArrayList<BusStop>())
        return gSon.fromJson(
            pref.getString(TIME_ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        )
    }

    fun saveLocationAlarm(pref: SharedPreferences, busStopList : List<BusStop>) {
        val busStops = gSon.toJson(busStopList)
        pref.edit().putString(LOCATION_ALARM_ID,busStops).apply()
    }

    fun loadLocationAlarmID (pref: SharedPreferences) : MutableList<BusStop> {
        val emptyList = gSon.toJson(ArrayList<BusStop>())
        return gSon.fromJson(
            pref.getString(LOCATION_ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        )
    }

}