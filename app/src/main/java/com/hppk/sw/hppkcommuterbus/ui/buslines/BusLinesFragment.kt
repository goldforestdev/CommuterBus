package com.hppk.sw.hppkcommuterbus.ui.buslines


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.ui.buslines.details.LineDetailsActivity
import kotlinx.android.synthetic.main.fragment_bus_lines.*

class BusLinesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bus_lines, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTemp.setOnClickListener {
            startActivity(Intent(context, LineDetailsActivity::class.java))
        }
    }

}
