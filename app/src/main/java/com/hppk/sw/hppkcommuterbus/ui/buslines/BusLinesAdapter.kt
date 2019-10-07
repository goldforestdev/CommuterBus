package com.hppk.sw.hppkcommuterbus.ui.buslines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.util.Utils
import kotlinx.android.synthetic.main.item_bus_list.view.*

class BusLinesAdapter(
    val busLines : MutableList<BusLine> = mutableListOf(),
    private var context : Context? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return BusLinesHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bus_list, parent, false))
    }



    override fun getItemCount(): Int  = busLines.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BusLinesHolder) {
            with (holder) {
                if (Utils.getLanguage(context!!) != "ko") {
                    tvBusLineName.text = busLines[position].name
                    tvBusStart.text =
                        "${busLines[position].busStops[0].name} ${context!!.resources.getString(R.string.start)}"
                } else {
                    tvBusLineName.text = busLines[position].nameKr
                    tvBusStart.text =
                        "${busLines[position].busStops[0].nameKr} ${context!!.resources.getString(R.string.start)}"
                }
            }
        }
    }





    class BusLinesHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvBusLineName : TextView = itemView.tvBusLineName
        val tvBusStart : TextView = itemView.tvBusStart
    }
}