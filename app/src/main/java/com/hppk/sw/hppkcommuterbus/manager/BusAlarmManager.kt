package com.hppk.sw.hppkcommuterbus.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.receiver.AlarmReceiver
import com.hppk.sw.hppkcommuterbus.receiver.GeofenceBroadcastReceiver
import com.hppk.sw.hppkcommuterbus.receiver.KEY_ALARM_BUS_STOP
import com.hppk.sw.hppkcommuterbus.receiver.KEY_ALARM_MESSAGE
import java.text.SimpleDateFormat
import java.util.*


class BusAlarmManager(
    private val context: Context
) {

    private val TAG = BusAlarmManager::class.java.simpleName
    private val alarmManager: AlarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(context)
    }

    fun register(alarmId: Int, busStop: BusStop, time: Long, msg: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(KEY_ALARM_MESSAGE, msg)

        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        busStop.time.split(":").let {
            calendar.set(Calendar.HOUR_OF_DAY, it[0].toInt())
            calendar.set(Calendar.MINUTE, it[1].toInt())
        }

        if (calendar.timeInMillis < now) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis - time, pendingIntent)
    }

    fun register(alarmId: Int, busStop: BusStop, msg: String) {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
            .putExtra(KEY_ALARM_MESSAGE, msg)

        val geofence = Geofence.Builder()
            .setRequestId(busStop.name)
            .setNotificationResponsiveness(0)
            .setCircularRegion(busStop.lat, busStop.lng, 1000f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofenceRequest = getGeofencingRequest(geofence)
        val geofencePendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)?.run {
            addOnSuccessListener {
                Log.d(TAG, "[BUS] register - geofence: success")
            }
            addOnFailureListener { t ->
                Log.e(TAG, "[BUS] register - geofence: failed: ${t.message}", t)
            }
        }
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest =
        GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

    fun unregister(alarmId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun unregister(busStop: BusStop) {
        geofencingClient.removeGeofences(listOf(busStop.name))?.run {
            addOnSuccessListener {
                Log.d(TAG, "[BUS] unregister - geofence: success")
            }
            addOnFailureListener { t ->
                Log.e(TAG, "[BUS] unregister - geofence: failed: ${t.message}", t)
            }
        }
    }

}