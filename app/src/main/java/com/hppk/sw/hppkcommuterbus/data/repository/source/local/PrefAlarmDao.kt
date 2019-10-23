package com.hppk.sw.hppkcommuterbus.data.repository.source.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmDataSource
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteDataSource
import io.reactivex.Completable
import io.reactivex.Single

private const val ALARM_ID = "ALARM_ID"

class PrefAlarmDao(
    private val pref: SharedPreferences,
    private val gson: Gson = Gson()
) : AlarmDataSource {

    override fun getAll() = Single.create<List<BusStop>> { emitter ->
        val emptyList = gson.toJson(ArrayList<BusStop>())
        val busLines: ArrayList<BusStop> = gson.fromJson(
            pref.getString(ALARM_ID, emptyList),
            object : TypeToken<ArrayList<BusStop>>() {
            }.type
        ) ?: arrayListOf()

        emitter.onSuccess(busLines.toList())
    }

    override fun save(favorites: List<BusStop>) = Completable.create { emitter ->
        val busLines = gson.toJson(favorites)
        if (pref.edit().putString(ALARM_ID, busLines).commit()) {
            emitter.onComplete()
        } else {
            emitter.onError(Exception("Saving alarms is failed"))
        }
    }

}