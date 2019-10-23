package com.hppk.sw.hppkcommuterbus.ui.buslines


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.Type
import com.hppk.sw.hppkcommuterbus.data.repository.FavoriteRepository
import com.hppk.sw.hppkcommuterbus.data.repository.source.local.PrefFavoriteDao
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import com.hppk.sw.hppkcommuterbus.ui.details.BUS_LINE
import com.hppk.sw.hppkcommuterbus.ui.details.LineDetailsActivity
import kotlinx.android.synthetic.main.fragment_bus_lines.*

class BusLinesFragment : Fragment(), BusLinesContract.View, BusLinesAdapter.BusLineClickListener, BusLinesAdapter.BusFavoritesClickLister{

    private val presenter: BusLinesContract.Presenter by lazy {
        BusLinesPresenter(this, FavoriteRepository(PrefFavoriteDao(PreferenceManager.getDefaultSharedPreferences(context))))
    }
    private val busLinesAdapter: BusLinesAdapter by lazy { BusLinesAdapter(busLineClickListener = this, busFavoritesClickListener = this) }
    private val pref: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }
    private lateinit var map : Map<Type, List<BusLine>>
    private lateinit var favoritesBusLineList :MutableList<BusLine>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bus_lines, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onFavoritesListLoaded(favoritesList : List<BusLine>) {
        favoritesBusLineList = favoritesList.toMutableList()
        initRecyclerView()
        initRadioGroup()
    }

    private fun initRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, i ->
            busLinesAdapter.busLines.clear()
            when (i) {
                R.id.rbGoHome -> busLinesAdapter.busLines.addAll(map.getValue(Type.LEAVE_OFFICE))
                R.id.rbGoOffice -> busLinesAdapter.busLines.addAll(map.getValue(Type.GO_OFFICE))
            }
            busLinesAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView(){
        rcBusList.adapter = busLinesAdapter
        rcBusList.layoutManager = LinearLayoutManager(activity)
        busLinesAdapter.busLines.clear()
        busLinesAdapter.busLines.addAll(map.getValue(Type.GO_OFFICE))

        busLinesAdapter.favorites.clear()
        busLinesAdapter.favorites.addAll(favoritesBusLineList)

        busLinesAdapter.notifyDataSetChanged()
    }

    private fun initData () {
        activity?.apply {
            if (this is MainActivity) {
                map = this.busLineData.groupBy { it.type }
            }
        }
        presenter.loadRecent(pref)
    }

    override fun onBusLineClick(busLine: BusLine) {
        startActivity(
            Intent(context, LineDetailsActivity::class.java)
                .putExtra(BUS_LINE, busLine)
        )
    }

    override fun onBusFavoritesClick(busLine: BusLine) {
        if (favoritesBusLineList.contains(busLine)) {
            favoritesBusLineList.remove(busLine)
        } else {
            favoritesBusLineList.add(busLine)
        }
        busLinesAdapter.favorites.clear()
        busLinesAdapter.favorites.addAll(favoritesBusLineList)
        busLinesAdapter.notifyDataSetChanged()

        presenter.saveFavorites(favoritesBusLineList)
    }

    override fun onFavoritesSaved() {
        // TODO("not implemented")
    }
}
