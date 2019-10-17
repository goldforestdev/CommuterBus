package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

interface LineDetailsContract {

    interface View {
        fun onAlarmListLoaded(alarmBusStopList : MutableList<BusStop>)
    }

    interface Presenter {
        fun loadAlarmList(pref: SharedPreferences)
    }
}