package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

object LocalDataSource {
    private const val FAVORITES_ID = "FAVORITES_ID"
    private const val ALARM_ID = "ALARM_ID"


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