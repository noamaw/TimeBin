package com.noam.timebin.ui

import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.noam.timebin.MainActivity
import com.noam.timebin.ui.fragments.ServiceFragment
import com.noam.timebin.ui.fragments.TodayFragment

class TabsFragment {

    companion object {
        fun getInstance(mainActivity: MainActivity, position: Int): Fragment {
            return when (position) {
                0 -> ServiceFragment(mainActivity)
                else -> TodayFragment()
            }
        }
    }

//        return when (position) {
//            0 -> inflater.inflate(R.layout.service_fragment_layout, container, false)
//            1 -> inflater.inflate(R.layout.today_fragment_layout, container, false)
//            2 -> inflater.inflate(R.layout.week_fragment_layout, container, false)
//            3 -> inflater.inflate(R.layout.settings_fragment_layout, container, false)
//            else -> inflater.inflate(R.layout.service_fragment_layout, container, false)
//        }
}