package com.hppk.sw.hppkcommuterbus.ui.details

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.PrefAlarmDao
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import kotlinx.android.synthetic.main.activity_line_details.*
import net.daum.mf.map.api.*


const val BUS_LINE = "busline"

class LineDetailsActivity : AppCompatActivity(), BusStopsAdapter.BusStopClickListener
    , BusStopsAdapter.BusAlarmClickListener, LineDetailsContract.View {

    private val presenter: LineDetailsContract.Presenter by lazy {
        LineDetailsPresenter(this,
            AlarmRepository(PrefAlarmDao(PreferenceManager.getDefaultSharedPreferences(this)))
        )
    }
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

        val busLine = intent.getParcelableExtra<BusLine>(BUS_LINE)
        initBusLineMap(busLine)
        initBusLineList(busLine)
        initData()

        mapContainer.addView(mapView)
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onAlarmListSaved() {
        // TODO
    }

    private fun initBusLineList(busLine: BusLine) {
        busLinesAdapter = BusStopsAdapter(busLine.busStops, context = this, busType = busLine.type
            ,clickListener = this, alarmClickListener = this)
        rvBusStops.adapter = busLinesAdapter
        rvBusStops.layoutManager = LinearLayoutManager(this)
    }

    private fun initData () {
        presenter.loadAlarmList()
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
                    itemName = busStop.busStopName
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



    override fun onAlarmListLoaded(alarmBusStopList: List<BusStop>) {
        alarmList = alarmBusStopList.toMutableList()

        busLinesAdapter.alarmBusStops.clear()
        busLinesAdapter.alarmBusStops.addAll(alarmList)

        busLinesAdapter.notifyDataSetChanged()
    }

    override fun onBusStopClicked(busStop: BusStop) {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        mapView.poiItems.first {
            it.itemName == busStop.busStopName
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

    override fun onBusAlarmClicked (busStops: BusStop) {

        val message  = busStops.busStopName
        val alarmManager = BusAlarmManager(this)
        if (busStops.type == Type.GO_OFFICE) {
            if (alarmList.contains(busStops)) {
                alarmList.remove(busStops)
                alarmManager.unregister(busStops.index)
            } else {
                alarmList.add(busStops)
                alarmManager.register(busStops.index,busStops,5*60*1000,message)
            }
            presenter.saveAlarms(alarmList)
            busLinesAdapter.alarmBusStops.clear()
            busLinesAdapter.alarmBusStops.addAll(alarmList)
        } else {
            if (alarmList.contains(busStops)) {
                alarmList.remove(busStops)
                alarmManager.unregister(busStops)
            } else {
                alarmList.add(busStops)
                alarmManager.register(busStops.index,busStops,message)
            }
            presenter.saveAlarms(alarmList)
            busLinesAdapter.alarmBusStops.clear()
            busLinesAdapter.alarmBusStops.addAll(alarmList)
        }

        busLinesAdapter.notifyDataSetChanged()
    }

}
