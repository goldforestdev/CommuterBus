package com.hppk.sw.hppkcommuterbus.ui.buslines.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hppk.sw.hppkcommuterbus.R
import kotlinx.android.synthetic.main.activity_line_details.*
import net.daum.mf.map.api.MapView

class LineDetailsActivity : AppCompatActivity() {

    private val mapView: MapView by lazy { MapView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_details)
    }

    override fun onStart() {
        super.onStart()
        mapContainer.addView(mapView)
    }
}
