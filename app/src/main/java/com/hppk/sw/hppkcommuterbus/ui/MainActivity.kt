package com.hppk.sw.hppkcommuterbus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine


const val KEY_BUS_LINES = "KEY_BUS_LINES"

class MainActivity : AppCompatActivity() {
    private var busLineDBData: MutableList<BusLine> = mutableListOf()

    val busLineData: MutableList<BusLine>
        get() = busLineDBData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.hasExtra(KEY_BUS_LINES)) {
            intent.getParcelableArrayExtra(KEY_BUS_LINES)?.let { busLines ->
                busLines.map { it as BusLine }
            }?.let { busLines ->
                busLineDBData.addAll(busLines)
            }
        }

        findViewById<BottomNavigationView>(R.id.bottomNavView).setupWithNavController(
            findNavController(R.id.navHostFragment)
        )
    }

}
