package com.hppk.sw.hppkcommuterbus.ui.mypage


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusStop
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment(), MyPageContract.View {

    private val pref: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }
    private val presenter: MyPageContract.Presenter by lazy { MyPagePresenter(this) }
    private lateinit var myPageAdapter: MyPageAdapter
    private lateinit var favoritesBusLineList :MutableList<BusLine>
    private lateinit var timeAlarmBusStopList :MutableList<BusStop>
    private lateinit var locationAlarmBusStopList :MutableList<BusStop>

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }

    private fun initData () {
        presenter.loadData(pref)
    }



    override fun onDataLoaded(favoritesList : MutableList<BusLine>, timeAlarmList : MutableList<BusStop>
                              , locationAlarmList : MutableList<BusStop>) {
        favoritesBusLineList = favoritesList
        timeAlarmBusStopList = timeAlarmList
        locationAlarmBusStopList = locationAlarmList

        initRecyclerView()
    }

    private fun initRecyclerView(){

        val list = mutableListOf<Any>()
        list.add("title")
        list.addAll(timeAlarmBusStopList)
        list.add("title2")
        list.addAll(locationAlarmBusStopList)
        list.add("title3")
        list.addAll(favoritesBusLineList)
        myPageAdapter = MyPageAdapter(list = list)
        rcMyPageList.adapter = myPageAdapter
        rcMyPageList.layoutManager = LinearLayoutManager(activity)


        myPageAdapter.notifyDataSetChanged()
    }


}
