package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.local.LocalDataSource
import com.hppk.sw.hppkcommuterbus.data.model.BusLine

class BusLinesPresenter (
    private val view : BusLinesContract.View
) : BusLinesContract.Presenter {
    private lateinit var dataList :MutableList<BusLine>

    override fun loadRecent(pref:SharedPreferences) {
        dataList = LocalDataSource.loadFavoriteID(pref)
        view.onFavoritesListLoaded(dataList)
    }

}