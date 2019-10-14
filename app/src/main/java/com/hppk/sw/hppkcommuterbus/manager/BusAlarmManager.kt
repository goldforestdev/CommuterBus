package com.hppk.sw.hppkcommuterbus.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.receiver.*
import java.net.Socket


class BusAlarmManager(
    private val context: Context
) {
    private val alarmManager: AlarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val geofencingClient: GeofencingClient by lazy { LocationServices.getGeofencingClient(context) }

    fun register(alarmId: Int, busStop: BusStop, time: Long, msg: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(KEY_ALARM_MESSAGE, msg)
            .putExtra(KEY_ALARM_BUS_STOP, busStop)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun register(alarmId: Int, busStop: BusStop, msg: String) {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
            .putExtra(KEY_ALARM_MESSAGE, msg)
            .putExtra(KEY_ALARM_BUS_STOP, busStop)
        val geofencePendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val geofence = Geofence.Builder()
            .setRequestId(busStop.name)
            .setNotificationResponsiveness(0)
            .setCircularRegion(busStop.lat, busStop.lng, 1000f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofenceRequest = getGeofencingRequest(geofence)

        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()

    fun unregister(alarmId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun unregister(busStop: BusStop) {
        geofencingClient.removeGeofences(listOf(busStop.name))
    }

}