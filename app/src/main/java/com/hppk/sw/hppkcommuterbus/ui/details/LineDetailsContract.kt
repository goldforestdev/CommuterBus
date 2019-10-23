package com.hppk.sw.hppkcommuterbus.ui.details

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
        fun registerAlarm(busStop: BusStop)
        fun unregisterAlarm(busStop: BusStop)
    }
}