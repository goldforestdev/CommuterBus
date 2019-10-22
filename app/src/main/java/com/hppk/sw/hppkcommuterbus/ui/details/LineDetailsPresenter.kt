package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.local.LocalDataSource
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

class LineDetailsPresenter (
    private val view : LineDetailsContract.View
) : LineDetailsContract.Presenter {

    override fun loadAlarmList(pref: SharedPreferences) {
        val alarmDataList = LocalDataSource.loadAlarmList(pref)
        view.onAlarmListLoaded(alarmDataList)
    }
}