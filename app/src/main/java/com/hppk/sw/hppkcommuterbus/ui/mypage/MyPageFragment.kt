package com.hppk.sw.hppkcommuterbus.ui.mypage


import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.data.repository.AlarmRepository
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.PrefAlarmDao
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.PrefFavoriteDao
import com.hppk.sw.hppkcommuterbus.manager.BusAlarmManager
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import com.hppk.sw.hppkcommuterbus.ui.details.BUS_LINE
import com.hppk.sw.hppkcommuterbus.ui.details.LineDetailsActivity
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment(), MyPageContract.View, MyPageAdapter.BusLineClickLister
    , MyPageAdapter.BusStopClickListener, MyPageAdapter.BusFavoritesClickLister, MyPageAdapter.BusAlarmClickLister {

    private val presenter: MyPageContract.Presenter by lazy {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        MyPagePresenter(this,
            AlarmRepository(PrefAlarmDao(pref)),
            FavoriteRepository(PrefFavoriteDao(pref)),
            BusAlarmManager(activity!!)
        )
    }
    private lateinit var myPageAdapter: MyPageAdapter
    private lateinit var favoritesBusLineList :MutableList<BusLine>
    private lateinit var alarmBusLineList :MutableList<BusStop>
    private lateinit var busStopMap : Map<Type, List<BusStop>>
    private val TAG = MyPageFragment::class.java.simpleName

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

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    private fun initData () {
        presenter.loadData()
    }

    override fun onDataLoaded(favoritesList : List<BusLine>, alarmList : List<BusStop>){
        favoritesBusLineList = favoritesList.toMutableList()
        alarmBusLineList = alarmList.toMutableList()
        busStopMap = alarmBusLineList.groupBy { it.type }

        myPageAdapter.list = setMyPageData()
        myPageAdapter.notifyDataSetChanged()

        checkEmptyView()

    }

    override fun onFavoritesSaved() {
        // TODO
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

        val builder = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle(R.string.favorites)
        builder.setMessage(R.string.delete_favorite_bus)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            if (favoritesBusLineList.contains(busLine)) {
                favoritesBusLineList.remove(busLine)
                myPageAdapter.list.removeAt(index)
                if(favoritesBusLineList.size == 0) {
                    myPageAdapter.list.removeAt(index-1)
                }
                myPageAdapter.notifyDataSetChanged()
                presenter.saveFavorites(favoritesBusLineList)
            }

            checkEmptyView()
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder.show()
    }

    override fun onAlarmClick(index: Int, busStop: BusStop) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle(R.string.bus_alarm)
        builder.setMessage(R.string.delete_alarm_bus)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
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
            presenter.saveAlarms(alarmBusLineList)
            presenter.unregisterAlarm(busStop)

            checkEmptyView()
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder.show()

    }

    private fun checkEmptyView() {
        if (myPageAdapter.list.isEmpty()) {
            rcMyPageList.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
        } else {
            rcMyPageList.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
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
