package com.hppk.sw.hppkcommuterbus.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.receiver.*


class BusAlarmManager(
    private val context: Context
) {

    private val alarmManager: AlarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun register(alarmId: Int, busStop: BusStop, time: Long, msg: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(KEY_ALARM_MESSAGE, msg)
            .putExtra(KEY_ALARM_TYPE, ALARM_TYPE_GO_OFFICE)
            .putExtra(KEY_ALARM_BUS_STOP, busStop)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun unregister(alarmId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

}