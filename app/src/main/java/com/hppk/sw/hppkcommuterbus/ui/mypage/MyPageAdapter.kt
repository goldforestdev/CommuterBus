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
    var list : MutableList<Any> = mutableListOf(),
    private var context : Context? = null,
    private val busLineClickListener : BusLineClickLister,
    private val busStopClickListener : BusStopClickListener,
    private val busFavoritesClickLister: BusFavoritesClickLister,
    private val busAlarmClickLister: BusAlarmClickLister
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TITLE_CONTENT = 0
        const val ALARM_CONTENT = 1
        const val FAVORITE = 3
    }

    interface BusStopClickListener {
        fun onBusStopClick(busStop: BusStop)
    }

    interface BusLineClickLister {
        fun onBusLineClick(busLine: BusLine)
    }

    interface BusFavoritesClickLister {
        fun onFavoritesClick(index : Int, busLine : BusLine)
    }

    interface BusAlarmClickLister {
        fun onAlarmClick(index : Int, busStop : BusStop)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        when (viewType) {
            TITLE_CONTENT -> {
                view = inflater.inflate(R.layout.item_title, parent, false)
                return TitleHolder(view)
            }
            ALARM_CONTENT -> {
                view = inflater.inflate(R.layout.item_my_page_bus_stop, parent, false)
                return BusAlarmHolder(view)
            }
            FAVORITE -> {
                view = inflater.inflate(R.layout.item_bus_list, parent, false)
                return FavoriteHolder(view)
            }
            else -> {
                throw RuntimeException("Unknown Type Error")
            }
        }
    }
    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when {
            list[position] is String -> TITLE_CONTENT
            list[position] is BusStop -> ALARM_CONTENT
            list[position] is BusLine -> FAVORITE
            else -> super.getItemViewType(position)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = list[position]

        when (holder) {
            is TitleHolder -> with(holder) {
                tvTitle.text = (obj as String)
            }
            is BusAlarmHolder -> with(holder) {
                tvName.text =
                    if (CommuterBusApplication.language != "ko") (obj as BusStop).name else (obj as BusStop).nameKr

                tvTime.text = obj.time
                ivAlarm.setImageResource(R.drawable.ic_alarm_selected)
                ivAlarm.setOnClickListener { busAlarmClickLister.onAlarmClick(position,obj) }
                itemView.setOnClickListener { busStopClickListener.onBusStopClick(obj) }
            }
            is FavoriteHolder -> with(holder) {
                if (CommuterBusApplication.language != "ko") {
                    tvBusLineName.text = (obj as BusLine).name
                    tvBusStart.text =
                        "${obj.busStops[0].name} ${context!!.resources.getString(R.string.start)}"
                } else {
                    tvBusLineName.text = (obj as BusLine).nameKr
                    tvBusStart.text =
                        "${obj.busStops[0].nameKr} ${context!!.resources.getString(R.string.start)}"
                }
                ivStar.setImageResource(android.R.drawable.star_big_on)
                ivStar.setOnClickListener {
                    busFavoritesClickLister.onFavoritesClick(position,obj)
                }
                itemView.setOnClickListener {
                    busLineClickListener.onBusLineClick(obj)
                }
            }
        }
    }

    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
    }

    inner class BusAlarmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val tvTime: TextView = itemView.tvTime
        val ivAlarm: ImageView = itemView.ivAlarm
    }

    inner class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBusLineName: TextView = itemView.tvBusLineName
        val tvBusStart: TextView = itemView.tvBusStart
        val ivStar: ImageView = itemView.ivStar
    }

}
