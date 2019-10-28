package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.Type
import kotlinx.android.synthetic.main.item_bus_list.view.*

class BusLinesAdapter(
    val busLines : MutableList<BusLine> = mutableListOf(),
    val favorites : MutableList<BusLine> = mutableListOf(),
    private var context : Context? = null,
    private val busLineClickListener : BusLineClickListener,
    private val busFavoritesClickListener : BusFavoritesClickLister
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return BusLinesHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bus_list, parent, false))
    }

    interface BusLineClickListener {
        fun onBusLineClick(busLine: BusLine)
    }

    interface BusFavoritesClickLister {
        fun onBusFavoritesClick(busLine: BusLine)
    }

    override fun getItemCount(): Int  = busLines.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BusLinesHolder) {
            with (holder) {
                tvBusLineName.text = busLines[position].busLineName
                tvBusStart.text = "${busLines[position].busStops[0].busStopName} ${context!!.resources.getString(R.string.start)}"
                itemView.setOnClickListener {
                    busLineClickListener.onBusLineClick(busLines[position])
                }

                if (favorites.contains(busLines[position]) ) {
                    ivStar.setImageResource(R.drawable.ic_star_selected)
                } else {
                    ivStar.setImageResource(R.drawable.ic_star_border)
                }

                ivStar.setOnClickListener {
                    busFavoritesClickListener.onBusFavoritesClick(busLines[position])
                }
            }
        }
    }


    class BusLinesHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvBusLineName : TextView = itemView.tvBusLineName
        val tvBusStart : TextView = itemView.tvBusStart
        val ivStar : ImageView = itemView.ivStar
    }
}