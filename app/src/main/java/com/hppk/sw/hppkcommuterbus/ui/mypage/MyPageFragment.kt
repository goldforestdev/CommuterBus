package com.hppk.sw.hppkcommuterbus.ui.mypage


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.local.LocalDataSource
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import com.hppk.sw.hppkcommuterbus.ui.buslines.BusLinesAdapter
import com.hppk.sw.hppkcommuterbus.ui.details.BUS_LINE
import com.hppk.sw.hppkcommuterbus.ui.details.LineDetailsActivity
import com.hppk.sw.hppkcommuterbus.ui.splash.SplashPresenter
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment(), MyPageContract.View, MyPageAdapter.BusLineClickLister
    , MyPageAdapter.BusStopClickListener, MyPageAdapter.BusFavoritesClickLister, MyPageAdapter.BusAlarmClickLister {

    private val pref: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }
    private val presenter: MyPageContract.Presenter by lazy { MyPagePresenter(this) }
    private lateinit var myPageAdapter: MyPageAdapter
    private lateinit var favoritesBusLineList :MutableList<BusLine>
    private lateinit var alarmBusLineList :MutableList<BusStop>
    private lateinit var busStopMap : Map<Type, List<BusStop>>
    private val TAG = MyPageFragment::class.java.simpleName
    private var emptyView: View? = null

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initData()
    }

    override fun onResume() {
        super.onResume()

        initData()
    }

    private fun initData () {
        presenter.loadData(pref)
    }

    override fun onDataLoaded(favoritesList : MutableList<BusLine>, alarmList : MutableList<BusStop>){
        favoritesBusLineList = favoritesList
        alarmBusLineList = alarmList
        busStopMap = alarmBusLineList.groupBy { it.type }

        myPageAdapter.list.clear()
        myPageAdapter.list = setMyPageData()
        myPageAdapter.notifyDataSetChanged()

        setEmptyView()

    }

    private fun initRecyclerView(){

        myPageAdapter = MyPageAdapter(busLineClickListener = this
            , busStopClickListener = this, busFavoritesClickLister = this, busAlarmClickLister = this)
        rcMyPageList.layoutManager = LinearLayoutManager(activity)
        rcMyPageList.adapter = myPageAdapter
    }

    private fun setMyPageData(): MutableList<Any> {
        val list = mutableListOf<Any>()
        try {
            if (busStopMap.getValue(Type.GO_OFFICE).isNotEmpty()) {
                list.add(getString(R.string.go_to_office))
                list.addAll(busStopMap.getValue(Type.GO_OFFICE))
            }
        } catch (e: NoSuchElementException) {
            Log.e(TAG, e.message)
        }

        try {
            if (busStopMap.getValue(Type.LEAVE_OFFICE).isNotEmpty()) {
                list.add(getString(R.string.off_work))
                list.addAll(busStopMap.getValue(Type.LEAVE_OFFICE))
            }
        } catch (e: NoSuchElementException) {
            Log.e(TAG, e.message)
        }


        if (favoritesBusLineList.isNotEmpty()) {
            list.add(getString(R.string.favorites))
            list.addAll(favoritesBusLineList)
        }
        return list
    }


    override fun onBusStopClick(busStop: BusStop) {
        activity?.apply {
            if (this is MainActivity) {
                this.busLineData.firstOrNull { busLine ->
                    busLine.busStops.firstOrNull { busStopData ->
                        busStopData.index == busStop.index
                    } != null
                }?.let {
                    startActivity(
                        Intent(context, LineDetailsActivity::class.java)
                            .putExtra(BUS_LINE, it)
                    )
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

    override fun onFavoritesClick(index: Int, busLine: BusLine) {
        if (favoritesBusLineList.contains(busLine)) {
            favoritesBusLineList.remove(busLine)
            myPageAdapter.list.removeAt(index)
            if(favoritesBusLineList.size == 0) {
                myPageAdapter.list.removeAt(index-1)
            }
            myPageAdapter.notifyDataSetChanged()
            LocalDataSource.saveFavoriteID(pref,favoritesBusLineList)
        }

        setEmptyView()
    }

    override fun onAlarmClick(index: Int, busStop: BusStop) {
        if (busStop.type == Type.GO_OFFICE) {
            alarmListRemove(busStop, index)
            try {
                Log.i(TAG,getMapSize(Type.GO_OFFICE).toString())
            } catch (e : NoSuchElementException) {
                myPageAdapter.list.removeAt(index-1)
            }

        } else {
            alarmListRemove(busStop, index)
            try {
                Log.i(TAG,getMapSize(Type.LEAVE_OFFICE).toString())
            } catch (e : NoSuchElementException) {
                myPageAdapter.list.removeAt(index-1)
            }
        }
        myPageAdapter.notifyDataSetChanged()
        LocalDataSource.saveAlarm(pref, alarmBusLineList)

        setEmptyView()
    }

    private fun setEmptyView() {
        if (myPageAdapter.list.isEmpty()) {
            rcMyPageList.visibility = View.GONE
            ll_no_data.visibility = View.VISIBLE
        } else {
            rcMyPageList.visibility = View.VISIBLE
            ll_no_data.visibility = View.GONE
        }
    }

    private fun getMapSize(key : Type) : Int = busStopMap.getValue(key).size

    private fun alarmListRemove(
        busStop: BusStop,
        index: Int
    ) {
        if (alarmBusLineList.contains(busStop)) {
            alarmBusLineList.remove(busStop)
            myPageAdapter.list.removeAt(index)
            busStopMap = alarmBusLineList.groupBy { it.type }
        }
    }
}
