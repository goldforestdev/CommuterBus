package com.hppk.sw.hppkcommuterbus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.manager.NotiManager


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = GeofenceBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "[BUS] onReceive - error: ${geofencingEvent.errorCode}")
            return
        }

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val message = when {
                intent.hasExtra(KEY_ALARM_MESSAGE) -> intent.getStringExtra(KEY_ALARM_MESSAGE)
                else -> ""
            }
            NotiManager.notify(context, context.getString(R.string.bus_alarm), message)
        }
    }

}
