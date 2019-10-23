package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

interface LineDetailsContract {

    interface View {
        fun onAlarmListLoaded(alarmBusStopList : List<BusStop>)
        fun onAlarmListSaved()
    }

    interface Presenter {
        fun unsubscribe()
        fun loadAlarmList()
        fun saveAlarms(alarmList: List<BusStop>)
    }
}