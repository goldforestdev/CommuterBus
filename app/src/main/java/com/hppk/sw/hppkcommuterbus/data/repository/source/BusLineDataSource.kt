package com.hppk.sw.hppkcommuterbus.data.repository.source

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import io.reactivex.Single

interface BusLineDataSource {
    fun getBusLines(): Single<List<BusLine>>
    fun getBusLine(id: String): Single<BusLine>
}