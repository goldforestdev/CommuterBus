package com.hppk.sw.hppkcommuterbus.ui.buslines


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.firebase.FireBaseDB
import com.hppk.sw.hppkcommuterbus.ui.details.BUS_LINE
import com.hppk.sw.hppkcommuterbus.ui.details.LineDetailsActivity
import kotlinx.android.synthetic.main.fragment_bus_lines.*

class BusLinesFragment : Fragment(), BusLinesContract.View {
    private val mPresenter: BusLinesContract.Presenter by lazy { BusLinesPresenter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bus_lines, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTemp.setOnClickListener {
            val tempBusLine = BusLine(
                id = "Bus Line 6",
                name = "Bus Line 6",
                nameKr = "버스 노선 6",
                busStops = listOf(
                    BusStop("한솔아파트", "한솔아파트", "7:40", 37.236318, 127.025801),
                    BusStop("권곡사거리", "권곡사거리", "7:43", 37.246661, 127.032351),
                    BusStop("경기소방본부", "경기소방본부", "7:45", 37.254801, 127.036062),
                    BusStop("경기대 후문사거리", "경기대 후문사거리", "7:55", 37.296800, 127.040927),
                    BusStop("HP", "HP", "8:17", 37.394306, 127.110189)
                )
            )

            val tempBusLine2= BusLine(
                id = "Bus Line 6-1",
                name = "Bus Line 6-1",
                nameKr = "버스 노선 6-1",
                busStops = listOf(
                    BusStop("한솔아파트", "한솔아파트", "7:40", 37.236318, 127.025801),
                    BusStop("권곡사거리", "권곡사거리", "7:43", 37.246661, 127.032351),
                    BusStop("경기소방본부", "경기소방본부", "7:45", 37.254801, 127.036062),
                    BusStop("경기대 후문사거리", "경기대 후문사거리", "7:55", 37.296800, 127.040927),
                    BusStop("HP", "HP", "8:17", 37.394306, 127.110189)
                )
            )

            FireBaseDB.getInstance().postFireBaseDatabase(tempBusLine)
            FireBaseDB.getInstance().postFireBaseDatabase(tempBusLine2)
            startActivity(
                Intent(context, LineDetailsActivity::class.java)
                    .putExtra(BUS_LINE, tempBusLine)
            )
        }
    }

}
