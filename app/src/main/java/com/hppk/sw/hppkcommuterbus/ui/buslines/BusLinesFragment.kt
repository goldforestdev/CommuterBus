package com.hppk.sw.hppkcommuterbus.ui.buslines


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.firebase.FireBaseDB
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import com.hppk.sw.hppkcommuterbus.ui.details.BUS_LINE
import com.hppk.sw.hppkcommuterbus.ui.details.LineDetailsActivity
import kotlinx.android.synthetic.main.activity_line_details.*
import kotlinx.android.synthetic.main.fragment_bus_lines.*

class BusLinesFragment : Fragment(), BusLinesContract.View, BusLinesAdapter.BusLineClickListener{
    private val presenter: BusLinesContract.Presenter by lazy { BusLinesPresenter() }
    private val busLinesAdapter: BusLinesAdapter by lazy { BusLinesAdapter(busLineClickListener = this) }

    private val goOfficeBus : MutableList<BusLine> = mutableListOf<BusLine>()
    private val goHomeBus : MutableList<BusLine> = mutableListOf<BusLine>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bus_lines, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initRecyclerView()
        initRadioGroup()
    }

    private fun initRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, i ->
            busLinesAdapter.busLines.clear()
            when (i) {
                R.id.rbGoHome -> busLinesAdapter.busLines.addAll(goHomeBus)
                R.id.rbGoOffice -> busLinesAdapter.busLines.addAll(goOfficeBus)
            }
            busLinesAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView(){
        rcBusList.adapter = busLinesAdapter
        rcBusList.layoutManager = LinearLayoutManager(activity)
        busLinesAdapter.busLines.clear()
        busLinesAdapter.busLines.addAll(goOfficeBus)
        busLinesAdapter.notifyDataSetChanged()
    }

    private fun initData () {
        goOfficeBus.clear()
        goHomeBus.clear()
        activity?.apply {
            if (this is MainActivity) {
                for(data in this.busLineData) {
                    if (data.type == Type.GO_OFFICE) {
                        goOfficeBus.add(data)
                    } else {
                        goHomeBus.add(data)
                    }
                }
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
