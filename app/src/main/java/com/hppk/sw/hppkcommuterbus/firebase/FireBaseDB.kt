package com.hppk.sw.hppkcommuterbus.firebase

import com.google.firebase.database.FirebaseDatabase
import com.hppk.sw.hppkcommuterbus.data.model.BusLine

class FireBaseDB {
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.reference

    companion object {

        private val TAG = "FireBaseDB"
        @Volatile private var INSTANCE: FireBaseDB? = null

        fun getInstance(): FireBaseDB  =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?:FireBaseDB().also { INSTANCE = it }
            }

    }


    fun postFireBaseDatabase(busLine: BusLine) {
        ref.child("bus-line").child(busLine.id).setValue(busLine)
    }

}