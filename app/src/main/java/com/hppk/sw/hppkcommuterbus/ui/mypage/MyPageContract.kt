package com.hppk.sw.hppkcommuterbus.ui.mypage

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

interface MyPageContract {

    interface View {
        fun onDataLoaded(favoritesList : MutableList<BusLine>, alarmList : MutableList<BusStop>)
    }

    interface Presenter {
        fun loadData(pref: SharedPreferences)
    }
}