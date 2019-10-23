package com.hppk.sw.hppkcommuterbus.ui.mypage

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop

interface MyPageContract {

    interface View {
        fun onDataLoaded(favoritesList: List<BusLine>, alarmList: List<BusStop>)
        fun onFavoritesSaved()
    }

    interface Presenter {
        fun unsubscribe()
        fun loadData()
        fun saveAlarms(alarms: MutableList<BusStop>)
        fun saveFavorites(favorites: List<BusLine>)
    }
}