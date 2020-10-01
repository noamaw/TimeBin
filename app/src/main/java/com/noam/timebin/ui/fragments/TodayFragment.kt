package com.noam.timebin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.noam.timebin.R
import com.noam.timebin.TimeBinApplication
import com.noam.timebin.utils.convertLongToFormattedTime
import kotlinx.android.synthetic.main.today_fragment_layout.*

class TodayFragment : Fragment() {

    private lateinit var timerHeadTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.today_fragment_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        initUIObjects()
    }

    private fun initUIObjects() {
        timerHeadTextView = total_time_clock
        updateTotalTime()
    }

    private fun updateTotalTime() {
        timerHeadTextView.text = convertLongToFormattedTime(TimeBinApplication.getTimersTotalTime())
    }
}