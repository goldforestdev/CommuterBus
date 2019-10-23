package com.hppk.sw.hppkcommuterbus.data.repository

import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import io.reactivex.Completable
import io.reactivex.Single

class FavoriteRepository (
    private val localFavoriteDao: FavoriteDataSource,
    private val remoteFavoriteDao: FavoriteDataSource? = null
) {

    fun save(favoritesDataList : List<BusLine>): Completable {
        return localFavoriteDao.save(favoritesDataList)
    }

    fun getAll() : Single<List<BusLine>> {
        return localFavoriteDao.getAll()
    }
}

interface FavoriteDataSource {
    fun getAll(): Single<List<BusLine>>
    fun save(favorites: List<BusLine>): Completable
}