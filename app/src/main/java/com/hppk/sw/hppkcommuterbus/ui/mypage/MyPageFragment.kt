package com.hppk.sw.hppkcommuterbus.ui.mypage


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.hppk.sw.hppkcommuterbus.R
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment() {

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
        cl_timeAlarm.setOnClickListener {
            Toast.makeText(activity,"Time Alarm Setting ",Toast.LENGTH_LONG).show()
        }

        cl_locationAlarm.setOnClickListener {
            Toast.makeText(activity,"Location Alarm Setting ",Toast.LENGTH_LONG).show()
        }
    }




}
