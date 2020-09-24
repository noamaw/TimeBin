package com.noam.timebin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.noam.timebin.ui.TabsAdapter
import com.noam.timebin.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.service_fragment_layout.*


class MainActivity : AppCompatActivity() {

    private lateinit var tabsViewPager : ViewPager2
    private lateinit var tabsPageChangeCallback : ViewPager2.OnPageChangeCallback

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_SERVICE_TO_ACTIVITY -> {
                    val timePassed = intent.getLongExtra(EXTRA_TIME_PASSED, 0L)
                    setTimerView(timePassed)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TimeBinApplication.isActivityRunning = true
        sendRequestIntent()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver, IntentFilter(ACTION_SERVICE_TO_ACTIVITY))

        setViewPager()
    }

    private fun setViewPager() {
        val tabsNamesArray = resources.getStringArray(R.array.tabs_names)
        val tabsAdapter = TabsAdapter(this, tabsNamesArray.size)
        tabsViewPager = tabs_view_pager
        tabsViewPager.adapter = tabsAdapter
        tabsPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Toast.makeText(this@MainActivity, "Selected position: $position",
                    Toast.LENGTH_SHORT).show()
            }
        }
        tabsViewPager.registerOnPageChangeCallback(tabsPageChangeCallback)

        TabLayoutMediator(tabLayout, tabsViewPager) { tab, position ->
            tab.text = tabsNamesArray[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        TimeBinApplication.isActivityRunning = true
        sendRequestIntent()
    }

    override fun onDestroy() {
        tabsViewPager.unregisterOnPageChangeCallback(tabsPageChangeCallback)
        TimeBinApplication.isActivityRunning = false
        super.onDestroy()
    }

    private fun sendRequestIntent() {
        if (TimeBinApplication.isServiceRunning) {
            val intent = Intent(ACTION_REQUEST_TIME_PASSED)
            sendBroadcast(intent)
        }
    }
}
