package com.hppk.sw.hppkcommuterbus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.gson.Gson
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.commons.isHoliday
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.manager.NotiManager


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = GeofenceBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        if (isHoliday()) {
            return
        }

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "[BUS] onReceive - error: ${geofencingEvent.errorCode}")
            return
        }

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val busStop = Gson().fromJson(intent.getStringExtra(KEY_ALARM_BUS_STOP), BusStop::class.java)
            NotiManager.notify(context, context.getString(R.string.bus_alarm), context.getString(R.string.bus_alarm_go_home_distance, busStop.busStopName))
        }
    }

}
