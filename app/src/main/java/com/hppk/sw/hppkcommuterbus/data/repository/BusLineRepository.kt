package com.hppk.sw.hppkcommuterbus.data.repository

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.LocalBusLineDao
import com.hppk.sw.hppkcommuterbus.data.repository.source.remote.FirebaseBusLineDao
import io.reactivex.Single

class BusLineRepository(
    private val localBusLineDao: LocalBusLineDao,
    private val remoteBusLineDao: FirebaseBusLineDao = FirebaseBusLineDao()
) {

    fun getBusLines(): Single<List<BusLine>> {
        return remoteBusLineDao.getBusLines()
    }

    fun getBusLine(id: String): Single<BusLine> {
        return remoteBusLineDao.getBusLine(id)
    }
}