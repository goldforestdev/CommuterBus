package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.commons.getAlarmTime
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
    private val context: Context,
    private val alarmRepo: AlarmRepository,
    private val alarmManager: BusAlarmManager,
    private val pref: SharedPreferences,
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
            val time = pref.getInt(context.getString(R.string.key_alarm_go_office_time), 0) * 60 * 1000
            alarmManager.register(busStop.index, busStop, getAlarmTime(busStop) - time)
        } else {
            alarmManager.register(busStop.index, busStop)
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