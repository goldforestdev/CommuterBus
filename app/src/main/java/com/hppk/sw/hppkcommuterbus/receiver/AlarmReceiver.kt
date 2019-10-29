package com.hppk.sw.hppkcommuterbus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.commons.getTomorrowAlarmTime
import com.hppk.sw.hppkcommuterbus.commons.isHoliday
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.PrefAlarmDao
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import com.hppk.sw.hppkcommuterbus.manager.NotiManager
import io.reactivex.schedulers.Schedulers

const val KEY_ALARM_BUS_STOP = "KEY_ALARM_BUS_STOP"

class AlarmReceiver : BroadcastReceiver() {

    private val TAG = AlarmReceiver::class.java.simpleName

    private lateinit var busAlarmManager: BusAlarmManager
    private lateinit var pref: SharedPreferences
    private var minute: Int = 0
    private var time: Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        busAlarmManager = BusAlarmManager(context)
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        minute = pref.getInt(context.getString(R.string.key_alarm_go_office_time), 0)
        time = minute * 60 * 1000

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            handleBootCompleted(context)
        } else {
            handleAlarm(context, intent)
        }
    }

    private fun handleBootCompleted(context: Context) {
        AlarmRepository(PrefAlarmDao(pref)).getAll()
            .subscribeOn(Schedulers.io())
            .subscribe({
                it.forEach { busStop ->
                    if (busStop.type == Type.GO_OFFICE) {
                        busAlarmManager.register(busStop.index, busStop, getTomorrowAlarmTime(busStop) - time)
                    } else {
                        busAlarmManager.register(busStop.index, busStop)
                    }
                }
            }, { t ->
                Log.e(TAG, "[BUS] handleBootCompleted - failed: ${t.message}", t)
            })
    }

    private fun handleAlarm(context: Context, intent: Intent) {
        if (isHoliday()) {
            return
        }

        val busStop = Gson().fromJson(intent.getStringExtra(KEY_ALARM_BUS_STOP), BusStop::class.java)
        NotiManager.notify(context, context.getString(R.string.bus_alarm), context.getString(R.string.bus_alarm_go_office_time, busStop.busStopName, minute))
        registerNextAlarm(busStop, context, time.toLong())
    }

    private fun registerNextAlarm(busStop: BusStop, context: Context, time: Long) {
        busAlarmManager.register(busStop.index, busStop, getTomorrowAlarmTime(busStop) - time)
    }

}
