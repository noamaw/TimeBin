package com.noam.timebin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.noam.timebin.R

class TabsFragment(private val position: Int): Fragment() {

    companion object {
        fun getInstance(position: Int): Fragment {
            return TabsFragment(position)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (position) {
            0 -> inflater.inflate(R.layout.service_fragment_layout, container, false)
            1 -> inflater.inflate(R.layout.today_fragment_layout, container, false)
            2 -> inflater.inflate(R.layout.week_fragment_layout, container, false)
            3 -> inflater.inflate(R.layout.settings_fragment_layout, container, false)
            else -> inflater.inflate(R.layout.service_fragment_layout, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}