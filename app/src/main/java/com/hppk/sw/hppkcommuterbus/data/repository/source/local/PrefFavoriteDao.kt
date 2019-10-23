package com.hppk.sw.hppkcommuterbus.data.repository.source.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteDataSource
import io.reactivex.Completable
import io.reactivex.Single

private const val FAVORITES_ID = "FAVORITES_ID"

class PrefFavoriteDao(
    private val pref: SharedPreferences,
    private val gson: Gson = Gson()
) : FavoriteDataSource {

    override fun getAll() = Single.create<List<BusLine>> { emitter ->
        val emptyList = gson.toJson(ArrayList<BusLine>())
        val busLines: ArrayList<BusLine> = gson.fromJson(
            pref.getString(FAVORITES_ID, emptyList),
            object : TypeToken<ArrayList<BusLine>>() {
            }.type
        ) ?: arrayListOf()

        emitter.onSuccess(busLines.toList())
    }

    override fun save(favorites: List<BusLine>) = Completable.create { emitter ->
        val busLines = gson.toJson(favorites)
        if (pref.edit().putString(FAVORITES_ID, busLines).commit()) {
            emitter.onComplete()
        } else {
            emitter.onError(Exception("Saving favorites is failed"))
        }
    }

}