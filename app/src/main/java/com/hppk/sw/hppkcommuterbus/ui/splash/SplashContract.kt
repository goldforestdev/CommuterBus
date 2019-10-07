package com.hppk.sw.hppkcommuterbus.ui.splash

import com.hppk.sw.hppkcommuterbus.data.model.BusLine

interface SplashContract {
    interface View {
        fun onError(errTitle: Int, errMsg: Int)
        fun onBusLinesLoaded(busLines: List<BusLine>)
    }

    interface Presenter {
        fun unsubscribe()
        fun getBusLines()
    }
}