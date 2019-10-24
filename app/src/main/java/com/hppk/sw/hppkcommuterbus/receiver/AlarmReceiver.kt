package com.hppk.sw.hppkcommuterbus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.commons.getTomorrowAlarmTime
import com.hppk.sw.hppkcommuterbus.commons.isHoliday
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import com.hppk.sw.hppkcommuterbus.manager.NotiManager
import java.util.*

const val KEY_ALARM_BUS_STOP = "KEY_ALARM_BUS_STOP"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
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
        if (isHoliday()) {
            return
        }

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val minute = pref.getInt(context.getString(R.string.key_alarm_go_office_time), 0)
        val time = minute * 60 * 1000

        NotiManager.notify(context, context.getString(R.string.bus_alarm), context.getString(R.string.bus_alarm_go_office_time, minute))
        registerNextAlarm(intent, context, time.toLong())
    }

    private fun registerNextAlarm(intent: Intent, context: Context, time: Long) {
        val busStop = intent.getParcelableExtra<BusStop>(KEY_ALARM_BUS_STOP)
        BusAlarmManager(context).register(busStop.index, busStop, getTomorrowAlarmTime(busStop) - time)
    }

}
