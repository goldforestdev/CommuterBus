package com.hppk.sw.hppkcommuterbus.ui.current

import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.data.repository.BusLineRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.LocalBusLineDao
import com.hppk.sw.hppkcommuterbus.data.repository.source.remote.FirebaseBusLineDao
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


// Default location for HP Pangyo
private const val DEFAULT_LAT = 37.394365
private const val DEFAULT_LNG = 127.110520

class CurrentPresenter(
    private val view: CurrentContract.View,
    private val locationProviderClient: FusedLocationProviderClient,
    private val busLineRepository: BusLineRepository = BusLineRepository(LocalBusLineDao(), FirebaseBusLineDao()),
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposable: CompositeDisposable = CompositeDisposable()
) : CurrentContract.Presenter {

    private val TAG = CurrentPresenter::class.java.simpleName

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun getCurrentLocation() {
        disposable.add(
            getCurrentLocationPoint()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({ (lat, lng) ->
                    view.onCurrentLocationLoaded(lat, lng)
                }, { t ->
                    Log.e(TAG, "[KM] resetToCurrentLocation - failed: ${t.message}", t)
                })
        )
    }

    private fun getCurrentLocationPoint(): Observable<Pair<Double, Double>> =
        Observable.create<Pair<Double, Double>> { emitter ->
            locationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    DEFAULT_LAT to DEFAULT_LNG
                } else {
                    location.latitude to location.longitude
                }.let { point ->
                    emitter.onNext(point)
                }
            }.addOnFailureListener { t ->
                Log.e(TAG, "[BUS] getCurrentLocationPoint - failed: ${t.message}", t)
            }.addOnCanceledListener {
                Log.d(TAG, "[BUS] getCurrentLocationPoint - canceled")
            }.addOnCompleteListener {
                Log.d(TAG, "[BUS] getCurrentLocationPoint - complete")
            }
        }

    override fun getNearByBusStops(
        lat: Double,
        lng: Double,
        busLineData: MutableList<BusLine>,
        targetDistance: Float
    ) {
        val myLocation = Location("my_location").apply {
            latitude = lat
            longitude = lng
        }

        val nearByBusStops = busLineData.map { busLine ->
            val targetBusStops = busLine.busStops.take(busLine.busStops.size - 1)
                .filter { busStop ->
                    val locationB = Location("bus_stop").apply {
                        latitude = busStop.lat
                        longitude = busStop.lng
                    }

                    val distance = myLocation.distanceTo(locationB)
                    distance < targetDistance
                }

            busLine to targetBusStops
        }.toMap()

        view.onBusStopsLoaded(nearByBusStops)
        view.showNumberOfBusStops(nearByBusStops.values.flatten().size)
    }

}