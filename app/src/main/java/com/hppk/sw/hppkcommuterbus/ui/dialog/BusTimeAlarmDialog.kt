package com.hppk.sw.hppkcommuterbus.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.hppk.sw.hppkcommuterbus.R
import kotlinx.android.synthetic.main.fragment_bus_lines.*


class BusTimeAlarmDialog : DialogFragment() {

    enum class BusTimeAlarm {
        MINUTE_5,
        MINUTE_10,
        MINUTE_15,
        MINUTE_20
    }

    interface TimeAlarmCallback {
        fun onTimeAlarmSelected(timeAlarm: BusTimeAlarm)
    }

    private lateinit var mTimeAlarmSelectedCallback: TimeAlarmCallback
    private var selectedAlarm  : BusTimeAlarm = BusTimeAlarm.MINUTE_5


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.dialog_select_time_alarm, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initRadioGroup()
        if (context is TimeAlarmCallback) {
            mTimeAlarmSelectedCallback = context
        }
    }

    private fun initRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb5Minute -> selectedAlarm = BusTimeAlarm.MINUTE_5
                R.id.rb10Minute -> selectedAlarm = BusTimeAlarm.MINUTE_10
                R.id.rb15Minute -> selectedAlarm = BusTimeAlarm.MINUTE_15
                R.id.rb20Minute -> selectedAlarm = BusTimeAlarm.MINUTE_20
            }
        }
    }
}