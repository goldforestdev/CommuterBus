package com.hppk.sw.hppkcommuterbus.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.application.CommuterBusApplication
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import kotlinx.android.synthetic.main.item_bus_stop.view.*

class BusStopsAdapter (
    private val busStops: List<BusStop> = listOf(),
    val alarmBusStops : MutableList<BusStop> = mutableListOf(),
    private var context : Context? = null,
    private val busType : Type,
    private val clickListener: BusStopClickListener,
    private val alarmClickListener: BusAlarmClickListener
): RecyclerView.Adapter<BusStopsAdapter.BusStopHolder> () {

    interface BusStopClickListener {
        fun onBusStopClicked(busStop: BusStop)
    }

    interface BusAlarmClickListener {
        fun onBusAlarmClicked (busStops: BusStop)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopHolder =
        BusStopHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bus_stop, parent, false))

    override fun getItemCount(): Int = busStops.size

    override fun onBindViewHolder(holder: BusStopHolder, position: Int) {
        val busStop = busStops[position]

        when(position) {
            0 -> holder.itemView.viewTopBar.visibility = View.INVISIBLE
            busStops.size - 1 -> holder.itemView.viewBottomBar.visibility = View.INVISIBLE
        }

        holder.itemView.tvName.text = if (CommuterBusApplication.language!= "ko") {
            busStop.name
        } else {
            busStop.nameKr
        }
        if (busStop.index == 0) {
            holder.itemView.ivAlarm.visibility = View.GONE
        } else {
            holder.itemView.ivAlarm.visibility = View.VISIBLE
            if (alarmBusStops.contains(busStop)) {
                holder.itemView.ivAlarm.setImageResource(R.drawable.ic_alarm_selected)
            } else {
                holder.itemView.ivAlarm.setImageResource(R.drawable.ic_alarm_normal)
            }
        }


        holder.itemView.tvTime.text = busStop.time

        holder.itemView.setOnClickListener { clickListener.onBusStopClicked(busStop) }

        holder.itemView.ivAlarm.setOnClickListener { alarmClickListener.onBusAlarmClicked(busStop)}
    }

    class BusStopHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}