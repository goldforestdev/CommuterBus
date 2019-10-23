package com.hppk.sw.hppkcommuterbus.data.repository

import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import io.reactivex.Completable
import io.reactivex.Single

class AlarmRepository(
    private val localAlarmDao: AlarmDataSource,
    private val remoteAlarmDao: AlarmDataSource? = null
) {

    fun save(busStopList: List<BusStop>): Completable {
        return localAlarmDao.save(busStopList)
    }

    fun save(busStop: BusStop): Completable {
        return localAlarmDao.save(busStop)
    }

    fun getAll(): Single<List<BusStop>> {
        return localAlarmDao.getAll()
    }

    fun delete(busStop: BusStop): Completable {
        return localAlarmDao.delete(busStop)
    }
}

interface AlarmDataSource {
    fun getAll(): Single<List<BusStop>>
    fun save(alarms: List<BusStop>): Completable
    fun save(alarm: BusStop): Completable
    fun delete(busStop: BusStop): Completable
}