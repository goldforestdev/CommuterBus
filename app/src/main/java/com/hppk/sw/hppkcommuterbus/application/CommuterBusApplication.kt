package com.hppk.sw.hppkcommuterbus.application

import android.content.Context
import android.os.Build
import androidx.multidex.MultiDexApplication
import java.util.*

class CommuterBusApplication : MultiDexApplication () {

    companion object {
        var language : String ? = null

        private var instance: CommuterBusApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        language = getLanguage()
    }

    private fun getLanguage(): String {
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }
        return locale.language
    }


}