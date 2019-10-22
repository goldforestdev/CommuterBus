package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.SharedPreferences

interface BusLinesContract {

    interface View {
        fun onFavoritesListLoaded(favoritesList: List<String>)
        fun onFavoritesSaved()
    }

    interface Presenter {
        fun unsubscribe()
        fun loadRecent(pref: SharedPreferences)
        fun saveFavoriteIds(favoritesBusLineList: List<String>)
    }
}