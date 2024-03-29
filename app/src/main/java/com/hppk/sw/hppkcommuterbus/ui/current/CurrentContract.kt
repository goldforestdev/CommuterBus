package com.hppk.sw.hppkcommuterbus.ui.current

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop


private const val DEFAULT_DISTANCE = 500F // 500m

interface CurrentContract {
    interface View {
        fun onCurrentLocationLoaded(lat: Double, lng: Double)
        fun onBusStopsLoaded(nearByBusStops: Map<BusLine, List<BusStop>>)
        fun showNumberOfBusStops(size: Int)
    }

    interface Presenter {
        fun unsubscribe()
        fun getCurrentLocation()
        fun getNearByBusStops(lat: Double, lng: Double, busLineData: MutableList<BusLine>, distance: Float = DEFAULT_DISTANCE)
    }
}