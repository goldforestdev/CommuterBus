package com.hppk.sw.hppkcommuterbus.ui.current


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_map.*
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class CurrentFragment : Fragment(), CurrentContract.View {

    private val presenter: CurrentContract.Presenter by lazy {
        CurrentPresenter(
            this,
            LocationServices.getFusedLocationProviderClient(activity!!)
        )
    }
    private val mapView: MapView by lazy { MapView(activity) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getCurrentLocation()
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onCurrentLocationLoaded(lat: Double, lng: Double) {
        val currentPoint = MapPoint.mapPointWithGeoCoord(lat, lng)
        mapView.setZoomLevel(3, true)
        mapView.setMapCenterPoint(currentPoint, true)

        val circle = MapCircle(currentPoint, 500, resources.getColor(R.color.colorAccent), resources.getColor(R.color.colorAccentTransparent))
        mapView.addCircle(circle)
        mapContainer.addView(mapView)

        val marker = MapPOIItem().apply {
            itemName = getString(R.string.current_location)
            mapPoint = currentPoint
            markerType = MapPOIItem.MarkerType.YellowPin
        }
        mapView.addPOIItem(marker)

        presenter.getNearByBusStops(lat, lng, (activity as MainActivity).busLineData)
    }

    override fun onBusStopsLoaded(nearByBusStops: Map<BusLine, List<BusStop>>) {
        nearByBusStops.keys.forEach { busLine ->
            nearByBusStops[busLine]?.forEach { busStop ->
                val busStopPoint = MapPoint.mapPointWithGeoCoord(busStop.lat, busStop.lng)
                val marker = MapPOIItem().apply {
                    itemName = "${busLine.nameKr} - ${busStop.nameKr}"
                    mapPoint = busStopPoint
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
                mapView.addPOIItem(marker)
            }
        }
    }

    override fun showNumberOfBusStops(size: Int) {
        when(size) {
            0 -> getString(R.string.msg_not_found_bus_stops)
            else -> getString(R.string.msg_found_bus_stops, size)
        }.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
    }

}
