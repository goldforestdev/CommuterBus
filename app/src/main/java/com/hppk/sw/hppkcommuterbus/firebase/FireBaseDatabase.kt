package com.hppk.sw.hppkcommuterbus.firebase

import com.google.firebase.database.FirebaseDatabase
import com.hppk.sw.hppkcommuterbus.data.model.BusLine

class FireBaseDatabase {
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.reference

    companion object {

        private val TAG = "FireBaseDatabase"
        @Volatile private var INSTANCE: FireBaseDatabase? = null


        fun getInstance(): FireBaseDatabase  =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?:FireBaseDatabase().also { INSTANCE = it }
            }

    }


    fun postFireBaseDatabase(busLine: BusLine) {
        ref.child("bus-line").child(busLine.id).setValue(busLine)
    }

}