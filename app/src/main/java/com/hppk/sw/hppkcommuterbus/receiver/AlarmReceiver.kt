package com.hppk.sw.hppkcommuterbus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.commons.getTomorrowAlarmTime
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import com.hppk.sw.hppkcommuterbus.manager.NotiManager
import java.util.*

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
        if (!isHoliday()) {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val time = pref.getLong(context.getString(R.string.key_alarm_go_office_time), 0L)
            val minute = (time / 1000 / 60).toInt()

            NotiManager.notify(context, context.getString(R.string.bus_alarm), context.getString(R.string.bus_alarm_go_office_time, minute))

            val busStop = intent.getParcelableExtra<BusStop>(KEY_ALARM_BUS_STOP)
            BusAlarmManager(context).register(busStop.index, busStop, getTomorrowAlarmTime(busStop) - time)
        }
    }

    private fun isHoliday(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SATURDAY,
            Calendar.SUNDAY -> true
            else -> false
        }
    }

}
