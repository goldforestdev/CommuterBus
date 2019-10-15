package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences

object AlarmLocalDataSource {
    private const val ALARM_ID = "ALARM_ID"


    fun saveAlarm(pref: SharedPreferences,favoritesDataList : List<String>) {
        val set = HashSet<String>()
        set.addAll(favoritesDataList)
        pref.edit().putStringSet(ALARM_ID, set).apply()

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