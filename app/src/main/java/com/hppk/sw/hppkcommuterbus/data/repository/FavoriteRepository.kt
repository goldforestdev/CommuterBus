package com.hppk.sw.hppkcommuterbus.data.repository

import io.reactivex.Completable
import io.reactivex.Single


class FavoriteRepository(
    private val localFavoriteDataSource: FavoriteDataSource,
    private val remoteFavoriteDataSource: FavoriteDataSource? = null
) {

    fun saveFavoriteID(favoritesDataList: List<String>): Completable {
        return localFavoriteDataSource.saveFavoriteId(favoritesDataList)
    }

    fun getFavoriteID(): Single<List<String>> {
        return localFavoriteDataSource.getFavoriteIds()
    }

}

interface FavoriteDataSource {
    fun saveFavoriteId(favoriteIds: List<String>): Completable
    fun getFavoriteIds(): Single<List<String>>
}