package com.hppk.sw.hppkcommuterbus.ui.mypage

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.local.LocalDataSource
import com.hppk.sw.hppkcommuterbus.data.model.BusLine

class MyPagePresenter (
    private val view : MyPageContract.View
) : MyPageContract.Presenter {

    override fun loadData(pref:SharedPreferences) {
        val favoriteList = LocalDataSource.loadFavoriteID(pref)
        val alarmDataList = LocalDataSource.loadAlarmList(pref)
        view.onDataLoaded(favoriteList, alarmDataList)
    }

}