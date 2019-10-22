package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.SharedPreferences
import android.util.Log
import com.hppk.sw.hppkcommuterbus.data.local.LocalDataSource
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BusLinesPresenter (
    private val view : BusLinesContract.View,
    private val favoriteRepository: FavoriteRepository,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposable: CompositeDisposable = CompositeDisposable()
) : BusLinesContract.Presenter {

    private val TAG = BusLinesPresenter::class.java.simpleName

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun saveFavoriteIds(favoritesBusLineList: List<String>) {
        disposable.add(
            favoriteRepository.saveFavoriteID(favoritesBusLineList)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onFavoritesSaved()
                }, { t ->
                    Log.e(TAG, "[BUS] saveFavoriteIds - failed:${t.message}", t)
                })
        )
    }

    private lateinit var dataList :MutableList<String>

    override fun loadRecent(pref:SharedPreferences) {
        disposable.add(
            favoriteRepository.getFavoriteID()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onFavoritesListLoaded(it)
                }, { t ->
                    Log.e(TAG, "[BUS] loadRecent - failed:${t.message}", t)
                })
        )
    }

}