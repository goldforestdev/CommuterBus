package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.application.CommuterBusApplication
import com.hppk.sw.hppkcommuterbus.data.local.AlarmLocalDataSource
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import kotlinx.android.synthetic.main.activity_line_details.*
import net.daum.mf.map.api.*


const val BUS_LINE = "busline"

class LineDetailsActivity : AppCompatActivity(), BusStopsAdapter.BusStopClickListener
    , BusStopsAdapter.BusAlarmClickListener, LineDetailsContract.View {
    private val presenter: LineDetailsContract.Presenter by lazy { LineDetailsPresenter(this) }
    private val pref: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    private val mapView: MapView by lazy { MapView(this) }
    private val behavior: BottomSheetBehavior<ConstraintLayout> by lazy { BottomSheetBehavior.from(bottomSheet) }
    private lateinit var busLinesAdapter: BusStopsAdapter
    private lateinit var alarmList :MutableList<BusStop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_details)

        if (!intent.hasExtra(BUS_LINE)) {
            finish()
            return
        }

        initData()

        val busLine = intent.getParcelableExtra<BusLine>(BUS_LINE)
        initBusLineMap(busLine)
        initBusLineList(busLine)

        mapContainer.addView(mapView)
    }

    private fun initBusLineList(busLine: BusLine) {
        busLinesAdapter = BusStopsAdapter(busLine.busStops, context = this, busType = busLine.type
            ,clickListener = this, alarmClickListener = this)
        rvBusStops.adapter = busLinesAdapter
        rvBusStops.layoutManager = LinearLayoutManager(this)
    }

    private fun initData () {
        presenter.loadAlarmList(pref)
    }

    private fun initBusLineMap(busLine: BusLine) {
        val polyline = MapPolyline()
        polyline.tag = 1000
        polyline.lineColor = resources.getColor(R.color.colorAccent)
        polyline.addPoints(
            busLine.busStops.map { busStop ->
                MapPoint.mapPointWithGeoCoord(busStop.lat, busStop.lng)
            }.toTypedArray()
        )
        mapView.addPolyline(polyline)

        mapView.addPOIItems(
            busLine.busStops.map { busStop ->
                MapPOIItem().apply {
                    itemName = if (CommuterBusApplication.language != "ko") {
                        busStop.name
                    } else {
                        busStop.nameKr
                    }

                    mapPoint = MapPoint.mapPointWithGeoCoord(busStop.lat, busStop.lng)
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
            }.toTypedArray()
        )

        val mapPointBounds = MapPointBounds(polyline.mapPoints)
        val padding = 200
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
    }

    override fun onAlarmListLoaded(alarmBusStopList: MutableList<BusStop>) {
        alarmList = alarmBusStopList
    }

    override fun onBusStopClicked(busStop: BusStop) {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        mapView.poiItems.first {
            it.itemName == if (CommuterBusApplication.language =="ko") busStop.nameKr else busStop.name
        }.let {
            mapView.selectPOIItem(it, true)

            val lat = if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                busStop.lat
            } else {
                busStop.lat - 0.05
            }
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, busStop.lng), true)
        }
    }

    override fun onBusAlarmClicked (busType: Type, busStops: BusStop) {

        val message  = if (CommuterBusApplication.language != "ko") busStops.name else busStops.nameKr
        val alarmManager = BusAlarmManager(this)
        if (alarmList.contains(busStops)) {
            alarmList.remove(busStops)
            if (busType == Type.GO_OFFICE) {
                alarmManager.unregister(busStops.index)
            } else {
                alarmManager.unregister(busStops)
            }
        } else {
            alarmList.add(busStops)
            if (busType == Type.GO_OFFICE) {
                alarmManager.register(busStops.index,busStops,5*60*1000,message)
            } else {
                alarmManager.register(busStops.index,busStops,message)
            }
        }
        busLinesAdapter.alarmBusStops.clear()
        busLinesAdapter.alarmBusStops.addAll(alarmList)
        busLinesAdapter.notifyDataSetChanged()
        AlarmLocalDataSource.saveAlarm(pref,alarmList)
    }

}
