package com.hppk.sw.hppkcommuterbus.ui.details

import android.util.Log
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LineDetailsPresenter(
    private val view: LineDetailsContract.View,
    private val alarmRepo: AlarmRepository,
    private val alarmManager: BusAlarmManager,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposable: CompositeDisposable = CompositeDisposable()
) : LineDetailsContract.Presenter {

    private val TAG = LineDetailsPresenter::class.java.simpleName

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadAlarmList() {
        disposable.add(
            alarmRepo.getAll()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onAlarmListLoaded(it)
                }, { t ->
                    Log.e(TAG, "[BUS] loadAlarmList - failed: ${t.message}", t)
                })
        )
    }

    override fun saveAlarms(alarmList: List<BusStop>) {
        disposable.add(
            alarmRepo.save(alarmList)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onAlarmListSaved()
                }, { t ->
                    Log.e(TAG, "[BUS] saveAlarms - failed: ${t.message}", t)
                })
        )
    }

    override fun registerAlarm(busStop: BusStop) {
        if (busStop.type == Type.GO_OFFICE) {
            alarmManager.register(busStop.index, busStop, 5 * 60 * 1000, busStop.busStopName)
        } else {
            alarmManager.register(busStop.index, busStop, busStop.busStopName)
        }

        saveAlarm(busStop)
    }

    override fun unregisterAlarm(busStop: BusStop) {
        if (busStop.type == Type.GO_OFFICE) {
            alarmManager.unregister(busStop.index)
        } else {
            alarmManager.unregister(busStop)
        }

        deleteAlarm(busStop)
    }

    private fun saveAlarm(busStops: BusStop) {
        disposable.add(
            alarmRepo.save(busStops)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onAlarmListSaved()
                }, { t ->
                    Log.e(TAG, "[BUS] saveAlarm - failed: ${t.message}", t)
                })
        )
    }

    private fun deleteAlarm(busStop: BusStop) {
        disposable.add(
            alarmRepo.delete(busStop)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onAlarmListSaved()
                }, { t ->
                    Log.e(TAG, "[BUS] deleteAlarm - failed: ${t.message}", t)
                })
        )
    }

}