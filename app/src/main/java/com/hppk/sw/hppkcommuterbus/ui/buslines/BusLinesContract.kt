package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusLine

interface BusLinesContract {

    interface View {
        fun onFavoritesListLoaded(favoritesList: List<BusLine>)
        fun onFavoritesSaved()
    }

    interface Presenter {
        fun unsubscribe()
        fun loadRecent(pref: SharedPreferences)
        fun saveFavorites(favorites: List<BusLine>)
    }
}