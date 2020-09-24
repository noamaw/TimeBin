package com.noam.timebin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.noam.timebin.R

class TodayFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.today_fragment_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        initUIObjects()
    }

    private fun initUIObjects() {

    }
}