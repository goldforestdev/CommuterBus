package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.SharedPreferences

interface BusLinesContract {

    interface View {
        fun onFavoritesListLoaded(favoritesList : MutableList<String>)
    }

    interface Presenter {
        fun loadRecent(pref: SharedPreferences)
    }
}