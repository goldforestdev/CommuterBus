package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

object LocalDataSource {
    private const val FAVORITES_ID = "FAVORITES_ID"
    private const val TIME_ALARM_ID = "TIME_ALARM_ID"
    private const val LOCATION_ALARM_ID = "LOCATION_ALARM_ID"


    fun saveFavoriteID(pref: SharedPreferences,favoritesDataList : List<String>) {
        val set = HashSet<String>()
        set.addAll(favoritesDataList)
        pref.edit().putStringSet(FAVORITES_ID, set).apply()

    }

    fun loadFavoriteID (pref: SharedPreferences) : MutableList<String> {
        val dataList = ArrayList<String>()

        val set = pref.getStringSet(FAVORITES_ID, null)
        if (set != null) {
            dataList.addAll(set)
        }
        return dataList
    }

    fun saveTimeAlarm(pref: SharedPreferences, busStopList : List<BusStop>) {
        val busStops = Gson().toJson(busStopList)
        pref.edit().putString(TIME_ALARM_ID,busStops).apply()
    }

    fun loadTimeAlarmID (pref: SharedPreferences) : MutableList<BusStop> {
        val emptyList = Gson().toJson(ArrayList<BusStop>())
        return Gson().fromJson(
            pref.getString(TIME_ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        )
    }

    fun saveLocationAlarm(pref: SharedPreferences, busStopList : List<BusStop>) {
        val busStops = Gson().toJson(busStopList)
        pref.edit().putString(LOCATION_ALARM_ID,busStops).apply()
    }

    fun loadLocationAlarmID (pref: SharedPreferences) : MutableList<BusStop> {
        val emptyList = Gson().toJson(ArrayList<BusStop>())
        return Gson().fromJson(
            pref.getString(LOCATION_ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        )
    }

}