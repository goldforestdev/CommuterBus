package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.hppk.sw.hppkcommuterbus.application.CommuterBusApplication
import org.json.JSONArray
import org.json.JSONException

object FavoritesLocalDataSource {
    private const val FAVORITES_ID = "FAVORITES_ID"
    private val pref: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(CommuterBusApplication.applicationContext()) }

    fun saveFavoriteID(favoritesDataList : ArrayList<String>) {
        val set = HashSet<String>()
        set.addAll(favoritesDataList)
        pref.edit().putStringSet(FAVORITES_ID, set).apply()

    }

    fun loadFavoriteID () : ArrayList<String> {
        val dataList = ArrayList<String>()

        val set = pref.getStringSet(FAVORITES_ID, null)
        if (set != null) {
            dataList.addAll(set)
        }

        return dataList
    }

}