package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

object AlarmLocalDataSource {
    private const val ALARM_ID = "ALARM_ID"


    fun saveAlarm(pref: SharedPreferences,busStopList : List<BusStop>) {
        val set = HashSet<BusStop>()
        set.addAll(busStopList)
        pref.edit().put(ALARM_ID, set).apply()
    }

    fun loadAlarmID (pref: SharedPreferences) : MutableList<String> {
        val dataList = ArrayList<String>()

        val set = pref.getStringSet(ALARM_ID, null)
        if (set != null) {
            dataList.addAll(set)
        }
        return dataList
    }

}