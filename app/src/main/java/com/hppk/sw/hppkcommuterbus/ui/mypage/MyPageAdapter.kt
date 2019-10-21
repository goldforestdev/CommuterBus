package com.hppk.sw.hppkcommuterbus.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.application.CommuterBusApplication
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import kotlinx.android.synthetic.main.item_bus_list.view.*
import kotlinx.android.synthetic.main.item_my_page_bus_stop.view.*
import kotlinx.android.synthetic.main.item_title.view.*

class MyPageAdapter(
    val timeBusAlarms : MutableList<BusStop> = mutableListOf(),
    val locationBusAlarms : MutableList<BusStop> = mutableListOf(),
    val favoriteBusLines : MutableList<BusLine> = mutableListOf(),
    private var context : Context? = null,
    private val busLineClickListener : BusLineClickListener,
    private val busFavoritesClickListener : BusFavoritesClickLister
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        TITLE_CONTENT,
        OFFICE_ALARM_CONTENT,
        HOME_ALARM_CONTENT,
        FAVORITE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View
        context = parent.context
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        when (viewType) {
            ViewType.TITLE_CONTENT.ordinal -> {
                view = inflater.inflate(R.layout.item_title, parent,false)
                return TitleHolder(view)
            }
            ViewType.OFFICE_ALARM_CONTENT.ordinal -> {
                view = inflater.inflate(R.layout.item_my_page_bus_stop, parent,false)
                return BusAlarmHolder(view)
            }
            ViewType.HOME_ALARM_CONTENT.ordinal -> {
                view = inflater.inflate(R.layout.item_my_page_bus_stop, parent,false)
                return BusAlarmHolder(view)
            }
            else -> {
                view = inflater.inflate(R.layout.item_bus_list, parent,false)
                return FavoriteHolder(view)
            }
        }
    }

    interface BusLineClickListener {
        fun onBusLineClick(busLine: BusLine)
    }

    interface BusFavoritesClickLister {
        fun onBusFavoritesClick(busLine: BusLine)
    }

    override fun getItemCount(): Int {
        var totalCount = 0

        if (timeBusAlarms.size != 0) {
            totalCount += (timeBusAlarms.size + 1)
        }

        if (locationBusAlarms.size != 0) {
            totalCount += (locationBusAlarms.size + 1)
        }

        if (favoriteBusLines.size != 0) {
            totalCount += (favoriteBusLines.size + 1)
        }

        return totalCount
    }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is FavoriteHolder) {
                with (holder) {
                    if (CommuterBusApplication.language != "ko") {
                        tvBusLineName.text = favoriteBusLines[position].name
                        tvBusStart.text =
                            "${favoriteBusLines[position].busStops[0].name} ${context!!.resources.getString(R.string.start)}"
                    } else {
                        tvBusLineName.text = favoriteBusLines[position].nameKr
                        tvBusStart.text =
                            "${favoriteBusLines[position].busStops[0].nameKr} ${context!!.resources.getString(R.string.start)}"
                    }
                    itemView.setOnClickListener {
                        busLineClickListener.onBusLineClick(favoriteBusLines[position])
                    }

                    if (favoriteBusLines.contains(favoriteBusLines[position]) ) {
                        ivStar.setImageResource(android.R.drawable.star_big_on)
                    } else {
                        ivStar.setImageResource(android.R.drawable.star_big_off)
                    }

                    ivStar.setOnClickListener {
                        busFavoritesClickListener.onBusFavoritesClick(busLines[position])
                    }
                }
            }
        }

        class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle : TextView = itemView.tvTitle
        }

        class BusAlarmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName : TextView = itemView.tvName
            val tvTime : TextView = itemView.tvTime
            val ivAlarm : ImageView = itemView.ivAlarm
        }

        class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvBusLineName : TextView = itemView.tvBusLineName
            val tvBusStart : TextView = itemView.tvBusStart
            val ivAlarm : ImageView = itemView.ivStar
        }


       /* class BusLinesHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val tvBusLineName : TextView = itemView.tvBusLineName
            val tvBusStart : TextView = itemView.tvBusStart
            val ivStar : ImageView = itemView.ivStar
        }*/
    }
}