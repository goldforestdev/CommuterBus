package com.hppk.sw.hppkcommuterbus.data.repository.source.local

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.repository.source.BusLineDataSource
import io.reactivex.Single

class LocalBusLineDao : BusLineDataSource {
    override fun getBusLines(): Single<List<BusLine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBusLine(id: String): Single<BusLine> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}