package com.hppk.sw.hppkcommuterbus.application

import android.os.Build
import androidx.multidex.MultiDexApplication
import com.hppk.sw.hppkcommuterbus.manager.NotiManager

var language: String? = null

class CommuterBusApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        language = getLanguage()
        NotiManager.createChannel(this)
    }

    private fun getLanguage() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> resources.configuration.locales.get(0)
        else -> resources.configuration.locale
    }.language


}