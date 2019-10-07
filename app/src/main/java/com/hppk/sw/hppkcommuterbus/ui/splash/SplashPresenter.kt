package com.hppk.sw.hppkcommuterbus.ui.splash

import android.util.Log
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.repository.BusLineRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.LocalBusLineDao
import com.hppk.sw.hppkcommuterbus.data.repository.source.remote.FirebaseBusLineDao
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashPresenter (
    private val view: SplashContract.View,
    private val busLineRepository: BusLineRepository = BusLineRepository(LocalBusLineDao(), FirebaseBusLineDao()),
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposable: CompositeDisposable = CompositeDisposable()
): SplashContract.Presenter {

    private val TAG = SplashPresenter::class.java.simpleName

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun getBusLines() {
        disposable.add(
            busLineRepository.getBusLines()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    view.onBusLinesLoaded(it)
                }, { t->
                    view.onError(R.string.unexpected_error, R.string.err_failed_to_get_bus_lines)
                    Log.e(TAG, "[BUS] getBusLines - failed: ${t.message}", t)
                })
        )
    }

}