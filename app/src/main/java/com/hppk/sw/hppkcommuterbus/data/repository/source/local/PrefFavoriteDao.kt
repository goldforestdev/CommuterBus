package com.hppk.sw.hppkcommuterbus.data.repository.source.local

import android.content.SharedPreferences
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteDataSource
import io.reactivex.Completable
import io.reactivex.Single

private const val FAVORITES_ID = "FAVORITES_ID"

class PrefFavoriteDao(
    private val pref: SharedPreferences
) : FavoriteDataSource {

    override fun saveFavoriteId(favoriteIds: List<String>) = Completable.create { emitter ->
        if (pref.edit().putStringSet(FAVORITES_ID, favoriteIds.toSet()).commit()) {
            emitter.onComplete()
        } else {
            emitter.onError(Exception("Saveing favorites failed"))
        }
    }

    override fun getFavoriteIds() = Single.create<List<String>> { emitter ->
        emitter.onSuccess(pref.getStringSet(FAVORITES_ID, setOf()).toList())
    }
}