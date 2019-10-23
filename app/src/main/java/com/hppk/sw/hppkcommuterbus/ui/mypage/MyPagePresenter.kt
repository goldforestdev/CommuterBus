package com.hppk.sw.hppkcommuterbus.ui.mypage

import android.util.Log
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteRepository
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MyPagePresenter(
    private val view: MyPageContract.View,
    private val alarmRepo: AlarmRepository,
    private val favoriteRepo: FavoriteRepository,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposable: CompositeDisposable = CompositeDisposable()
) : MyPageContract.Presenter {

    private val TAG = MyPagePresenter::class.java.simpleName

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadData() {
        val getFavorites = favoriteRepo.getAll()
        val getAlarms = alarmRepo.getAll()

        disposable.add(
            Single.zip(getFavorites, getAlarms,
                BiFunction<List<BusLine>, List<BusStop>, Pair<List<BusLine>, List<BusStop>>> { favorites, alarms ->
                    favorites to alarms
                })
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({ (favorites, alarms) ->
                    view.onDataLoaded(favorites, alarms)
                }, { t ->
                    Log.e(TAG, "[BUS] loadData - failed: $t.message", t)
                })
        )
    }

    override fun saveAlarms(alarms: MutableList<BusStop>) {
        disposable.add(
            alarmRepo.save(alarms)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onFavoritesSaved()
                }, { t ->
                    Log.e(TAG, "[BUS] saveAlarms - failed: $t.message", t)
                })
        )
    }

    override fun saveFavorites(favorites: List<BusLine>) {
        disposable.add(
            favoriteRepo.save(favorites)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onFavoritesSaved()
                }, { t ->
                    Log.e(TAG, "[BUS] saveFavorites - failed: $t.message", t)
                })
        )
    }
}