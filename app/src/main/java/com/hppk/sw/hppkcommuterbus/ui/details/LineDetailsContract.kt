package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

interface LineDetailsContract {

    interface View {
        fun onAlarmListLoaded(timeAlarmBusStopList : MutableList<BusStop>, locationAlarmBusStopList : MutableList<BusStop>)
    }

    interface Presenter {
        fun loadAlarmList(pref: SharedPreferences)
    }
}