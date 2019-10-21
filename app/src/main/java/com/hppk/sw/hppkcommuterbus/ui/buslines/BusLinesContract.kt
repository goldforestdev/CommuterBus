package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusLine

interface BusLinesContract {

    interface View {
        fun onFavoritesListLoaded(favoritesList : MutableList<BusLine>)
    }

    interface Presenter {
        fun loadRecent(pref: SharedPreferences)
    }
}