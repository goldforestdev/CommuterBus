package com.hppk.sw.hppkcommuterbus.ui.details

import android.util.Log
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LineDetailsPresenter(
    private val view: LineDetailsContract.View,
    private val alarmRepo: AlarmRepository,
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

}