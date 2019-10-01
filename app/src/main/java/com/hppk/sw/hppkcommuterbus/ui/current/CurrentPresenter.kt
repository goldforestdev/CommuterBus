package com.hppk.sw.hppkcommuterbus.ui.current

import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


// Default location for HP Pangyo
private const val DEFAULT_LAT = 37.394365
private const val DEFAULT_LNG = 127.110520


class CurrentPresenter (
    private val view: CurrentContract.View,
    private val locationProviderClient: FusedLocationProviderClient,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposable: CompositeDisposable = CompositeDisposable()
): CurrentContract.Presenter {

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

}