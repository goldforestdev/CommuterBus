package com.hppk.sw.hppkcommuterbus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.manager.NotiManager

const val KEY_ALARM_MESSAGE = "KEY_ALARM_MESSAGE"
const val KEY_ALARM_BUS_STOP = "KEY_ALARM_BUS_STOP"

class AlarmReceiver : BroadcastReceiver() {

    private val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "[BUS] onReceive")
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            handleBootCompleted(context)
        } else {
            handleAlarm(context, intent)
        }
    }

    private fun handleBootCompleted(context: Context) {
        // TODO: Alarm 등록
    }

    private fun handleAlarm(context: Context, intent: Intent) {
        val message = when {
            intent.hasExtra(KEY_ALARM_MESSAGE) -> intent.getStringExtra(KEY_ALARM_MESSAGE)
            else -> ""
        }
        Log.d(TAG, "[BUS] handleAlarm - message: $message")
        NotiManager.notify(context, context.getString(R.string.bus_alarm), message)
    }

}
