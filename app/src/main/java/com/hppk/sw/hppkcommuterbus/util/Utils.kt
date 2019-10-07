package com.hppk.sw.hppkcommuterbus.util

import android.content.Context
import android.os.Build
import java.util.*

object  Utils {
    fun getLanguage(context: Context): String {
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }

        return locale.language
    }
}