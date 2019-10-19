package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.local.LocalDataSource
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

class LineDetailsPresenter (
    private val view : LineDetailsContract.View
) : LineDetailsContract.Presenter {

    override fun loadAlarmList(pref: SharedPreferences) {
        val timeAlarmDataList = LocalDataSource.loadTimeAlarmID(pref)
        val locationAlarmDataList = LocalDataSource.loadLocationAlarmID(pref)
        view.onAlarmListLoaded(timeAlarmDataList, locationAlarmDataList)
    }
}