package com.hppk.sw.hppkcommuterbus.ui.mypage

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

data class MyPageModel(
    val type : Int,
    val titleText : String,
    val busStop: BusStop,
    val timeBusAlarms : MutableList<BusStop> = mutableListOf(),
    val locationBusAlarms : MutableList<BusStop> = mutableListOf(),
    val favoriteBusLines : MutableList<BusLine> = mutableListOf()) {
    companion object {
        const val TITLE_CONTENT = 0
        const val OFFICE_ALARM_CONTENT = 1
        const val HOME_ALARM_CONTENT = 2
        const val FAVORITE = 3
    }
}
