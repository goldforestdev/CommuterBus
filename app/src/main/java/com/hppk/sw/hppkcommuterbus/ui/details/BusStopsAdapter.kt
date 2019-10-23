package com.hppk.sw.hppkcommuterbus.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import kotlinx.android.synthetic.main.item_bus_stop.view.*

data class BusStopState(
    val busStop: BusStop,
    var isAlarmOn: Boolean = false
)

class BusStopsAdapter (
    val busStops: MutableList<BusStopState> = mutableListOf(),
    private val clickListener: BusStopClickListener,
    private val alarmClickListener: BusAlarmClickListener
): RecyclerView.Adapter<BusStopsAdapter.BusStopHolder> () {

    interface BusStopClickListener {
        fun onBusStopClicked(busStop: BusStop)
    }

    interface BusAlarmClickListener {
        fun onBusAlarmClicked (busStops: BusStop, alarmOn: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopHolder =
        BusStopHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bus_stop, parent, false))

    override fun getItemCount(): Int = busStops.size

    override fun onBindViewHolder(holder: BusStopHolder, position: Int) {
        val (busStop, isAlarmOn) = busStops[position]

        when(position) {
            0 -> holder.itemView.viewTopBar.visibility = View.INVISIBLE
            busStops.lastIndex -> holder.itemView.viewBottomBar.visibility = View.INVISIBLE
            else -> {
                holder.itemView.viewTopBar.visibility = View.VISIBLE
                holder.itemView.viewBottomBar.visibility = View.VISIBLE
            }
        }

        holder.itemView.tvName.text = busStop.busStopName
        if (busStop.index == 0) {
            holder.itemView.ivAlarm.visibility = View.GONE
        } else {
            holder.itemView.ivAlarm.visibility = View.VISIBLE
            val alarmImg = when {
                isAlarmOn -> R.drawable.ic_alarm_selected
                else -> R.drawable.ic_alarm_normal
            }
            holder.itemView.ivAlarm.setImageResource(alarmImg)
        }


        holder.itemView.tvTime.text = busStop.time

        holder.itemView.setOnClickListener { clickListener.onBusStopClicked(busStop) }

        holder.itemView.ivAlarm.setOnClickListener { alarmClickListener.onBusAlarmClicked(busStop, isAlarmOn)}
    }

    class BusStopHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}