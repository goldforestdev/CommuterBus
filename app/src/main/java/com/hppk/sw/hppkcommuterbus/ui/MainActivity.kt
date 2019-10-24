package com.hppk.sw.hppkcommuterbus.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.ui.settings.SettingsActivity


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menuSettings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        return true
    }

}
