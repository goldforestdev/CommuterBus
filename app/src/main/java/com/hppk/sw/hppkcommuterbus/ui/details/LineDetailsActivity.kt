package com.hppk.sw.hppkcommuterbus.ui.details

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.PrefAlarmDao
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import kotlinx.android.synthetic.main.activity_line_details.*
import net.daum.mf.map.api.*


const val BUS_LINE = "busline"

class LineDetailsActivity : AppCompatActivity(), BusStopsAdapter.BusStopClickListener
    , BusStopsAdapter.BusAlarmClickListener, LineDetailsContract.View {

    private val presenter: LineDetailsContract.Presenter by lazy {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        LineDetailsPresenter(this, this,
            AlarmRepository(PrefAlarmDao(pref)),
            BusAlarmManager(this),
            pref
        )
    }
    private val mapView: MapView by lazy { MapView(this) }
    private val behavior: BottomSheetBehavior<ConstraintLayout> by lazy { BottomSheetBehavior.from(bottomSheet) }
    private lateinit var busLinesAdapter: BusStopsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_details)

        if (!intent.hasExtra(BUS_LINE)) {
            finish()
            return
        }

        val busLine = intent.getParcelableExtra<BusLine>(BUS_LINE)
        initToolBar(busLine.busLineName)
        initBusLineMap(busLine)
        initBusLineList(busLine)
        initData()

        mapContainer.addView(mapView)
    }

    private fun initToolBar(busLineName: String) {
        title = busLineName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onAlarmListSaved() {
        // TODO
    }

    private fun initBusLineList(busLine: BusLine) {
        val busStopStates = busLine.busStops.map { BusStopState(it) }.toMutableList()
        busLinesAdapter = BusStopsAdapter(busStopStates, clickListener = this, alarmClickListener = this)
        rvBusStops.adapter = busLinesAdapter
        rvBusStops.layoutManager = LinearLayoutManager(this)
    }

    private fun initData () {
        presenter.loadAlarmList()
    }

    private fun initBusLineMap(busLine: BusLine) {
        val polyline = MapPolyline()
        polyline.tag = 1000
        polyline.lineColor = resources.getColor(R.color.colorPrimaryDark)
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
        val busStopStateList = busLinesAdapter.busStops.map { (busStop, _) ->
            BusStopState(busStop, alarmBusStopList.contains(busStop))
        }
        busLinesAdapter.busStops.clear()
        busLinesAdapter.busStops.addAll(busStopStateList)
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

    override fun onBusAlarmClicked (busStop: BusStop, alarmOn: Boolean) {
        if (alarmOn) {
            presenter.unregisterAlarm(busStop)
        } else {
            presenter.registerAlarm(busStop)
        }

        val position = busLinesAdapter.busStops.indexOfFirst { (b, _) -> b == busStop }
        busLinesAdapter.busStops[position] = BusStopState(busStop, !alarmOn)
        busLinesAdapter.notifyItemChanged(position)
    }

}
