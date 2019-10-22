package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

object LocalDataSource {
    private const val FAVORITES_ID = "FAVORITES_ID"
    private const val ALARM_ID = "ALARM_ID"
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

    fun saveAlarm(pref: SharedPreferences, busStopList : List<BusStop>) {
        val busStops = gSon.toJson(busStopList)
        pref.edit().putString(ALARM_ID,busStops).apply()
    }

    fun loadAlarmList (pref: SharedPreferences) : MutableList<BusStop> {
        val emptyList = gSon.toJson(ArrayList<BusStop>())
        return gSon.fromJson(
            pref.getString(ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        )
    }

}