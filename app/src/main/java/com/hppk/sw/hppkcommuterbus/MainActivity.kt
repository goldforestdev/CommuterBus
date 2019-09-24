package com.hppk.sw.hppkcommuterbus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.hppk.sw.hppkcommuterbus.const.DAUM_MAPS_ANDROID_APP_API_KEY
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        showSample()
    }


    private fun showSample() {
        val mapView = MapView(this)
        mapView.setDaumMapApiKey(DAUM_MAPS_ANDROID_APP_API_KEY)
        val mapViewContainer = findViewById(R.id.map_view) as ViewGroup
        val mapPoint = MapPoint.mapPointWithGeoCoord(37.394365, 127.110520)
        mapView.setMapCenterPoint(mapPoint, true)
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        mapViewContainer.addView(mapView)

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
