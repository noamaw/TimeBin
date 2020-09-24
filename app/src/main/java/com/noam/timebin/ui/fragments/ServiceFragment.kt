package com.noam.timebin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.noam.timebin.MainActivity
import com.noam.timebin.R
import com.noam.timebin.TimeBinApplication
import com.noam.timebin.TimerService
import com.noam.timebin.utils.ACTION_START_SERVICE
import com.noam.timebin.utils.ACTION_STOP_SERVICE
import com.noam.timebin.utils.convertLongToFormattedTime
import kotlinx.android.synthetic.main.service_fragment_layout.*

class ServiceFragment(activity: MainActivity) : Fragment() {

    private lateinit var actionServiceBtn : Button
    private lateinit var timerHeadTextView: TextView
    private lateinit var timerInfoTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.service_fragment_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setTimerView(timePassed: Long) {
        timerHeadTextView.text = resources.getString(R.string.time_passed_headline)
        timerInfoTextView.text = convertLongToFormattedTime(timePassed)
    }

    private fun initUIObjects() {
        actionServiceBtn = start_service_btn
        if (TimeBinApplication.isServiceRunning) {
            actionServiceBtn.text = resources.getString(R.string.stop_service_button)
        }
        actionServiceBtn.setOnClickListener {
            serviceBtnEvent()
        }
        timerHeadTextView = main_head_txt
        timerInfoTextView = main_timer_clock
    }

    private fun serviceBtnEvent() {
        if (TimeBinApplication.isServiceRunning) {
            stopTimerService()
            actionServiceBtn.text = resources.getString(R.string.start_service_button)
        } else {
            startTimerService()
            actionServiceBtn.text = resources.getString(R.string.stop_service_button)
        }
    }

    private fun stopTimerService() {
        val stopIntent = Intent(activity?.applicationContext, TimerService::class.java)
        stopIntent.action = ACTION_STOP_SERVICE
        activity?.stopService(stopIntent)
    }

    private fun startTimerService() {
        val startIntent = Intent(activity?.applicationContext, TimerService::class.java)
        startIntent.action = ACTION_START_SERVICE
        activity?.startService(startIntent)
    }
}