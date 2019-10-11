package com.hppk.sw.hppkcommuterbus.data.local


import android.content.SharedPreferences

object FavoritesLocalDataSource {
    private const val FAVORITES_ID = "FAVORITES_ID"


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

}