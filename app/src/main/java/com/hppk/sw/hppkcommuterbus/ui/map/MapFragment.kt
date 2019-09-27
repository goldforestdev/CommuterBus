package com.hppk.sw.hppkcommuterbus.ui.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hppk.sw.hppkcommuterbus.R
import kotlinx.android.synthetic.main.fragment_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment() {

    private val mapView: MapView by lazy { MapView(activity) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =// Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSample()
    }

    private fun showSample() {
        val mapPoint = MapPoint.mapPointWithGeoCoord(37.394365, 127.110520)
        mapView.setMapCenterPoint(mapPoint, true)
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        mapContainer.addView(mapView)

        val marker = MapPOIItem()
        marker.itemName = "판교알파돔타워"
        marker.tag = 0
        marker.mapPoint = mapPoint
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.markerType = MapPOIItem.MarkerType.BluePin
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)
    }

}
