package com.hppk.sw.hppkcommuterbus.ui.current


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.hppk.sw.hppkcommuterbus.R
import kotlinx.android.synthetic.main.fragment_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class CurrentFragment : Fragment(), CurrentContract.View {

    private val presenter: CurrentContract.Presenter by lazy { CurrentPresenter(this, LocationServices.getFusedLocationProviderClient(activity!!)) }
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
        val mapPoint = MapPoint.mapPointWithGeoCoord(lat, lng)
        mapView.setMapCenterPoint(mapPoint, true)
        mapContainer.addView(mapView)

        val marker = MapPOIItem()
        marker.itemName = "판교알파돔타워"
        marker.tag = 0
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)
    }

}
