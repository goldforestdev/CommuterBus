package com.hppk.sw.hppkcommuterbus.ui.buslines


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.firebase.FireBaseDB
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import com.hppk.sw.hppkcommuterbus.ui.details.BUS_LINE
import com.hppk.sw.hppkcommuterbus.ui.details.LineDetailsActivity
import kotlinx.android.synthetic.main.activity_line_details.*
import kotlinx.android.synthetic.main.fragment_bus_lines.*

class BusLinesFragment : Fragment(), BusLinesContract.View, BusLinesAdapter.BusLineClickListener{
    private val presenter: BusLinesContract.Presenter by lazy { BusLinesPresenter() }
    private val busLinesAdapter: BusLinesAdapter by lazy { BusLinesAdapter(busLineClickListener = this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bus_lines, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        rcBusList.adapter = busLinesAdapter
        rcBusList.layoutManager = LinearLayoutManager(activity)

        activity?.apply {
            if (this is MainActivity) {
                busLinesAdapter.busLines.clear()
                busLinesAdapter.busLines.addAll(this.busLineData)
                busLinesAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBusLineClick(busLine: BusLine) {
        startActivity(
            Intent(context, LineDetailsActivity::class.java)
                .putExtra(BUS_LINE, busLine)
        )
    }

}
