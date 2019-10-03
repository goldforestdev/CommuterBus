package com.hppk.sw.hppkcommuterbus.ui.current

interface CurrentContract {
    interface View {
        fun onCurrentLocationLoaded(lat: Double, lng: Double)
    }

    interface Presenter {
        fun unsubscribe()
        fun getCurrentLocation()
    }
}