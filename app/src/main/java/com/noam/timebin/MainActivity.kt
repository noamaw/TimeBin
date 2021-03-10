package com.noam.timebin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.noam.timebin.ui.TabsFragmentAdapter
import com.noam.timebin.utils.ACTION_REQUEST_TIME_PASSED
import com.noam.timebin.utils.ACTION_SERVICE_TO_ACTIVITY
import com.noam.timebin.utils.EXTRA_TIME_PASSED
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var tabsViewPager : ViewPager2
    private lateinit var tabsPageChangeCallback : ViewPager2.OnPageChangeCallback
//    private lateinit var myViewModel: MyViewModel

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_SERVICE_TO_ACTIVITY -> {
                    val timePassed = intent.getLongExtra(EXTRA_TIME_PASSED, 0L)
                    listener.setTimerView(timePassed)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
//        setObserving()
        TimeBinApplication.isActivityRunning = true
        sendRequestIntent()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver, IntentFilter(ACTION_SERVICE_TO_ACTIVITY))

        setViewPager()
    }

    private fun setObserving() {
//        myViewModel.getTimers()?.observe(this, Observer<List<MyTimer?>?> {
//            "Not yet implemented"
//        })
    }

    private fun setViewPager() {
        val tabsNamesArray = resources.getStringArray(R.array.tabs_names)
        val tabsAdapter = TabsFragmentAdapter(this, tabsNamesArray.size)
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

    // Interface
    interface MainActivityListener {
        fun setTimerView(timePassed : Long) {}
    }

    private class EmptyImplementationListener : MainActivityListener

    private var listener: MainActivityListener = EmptyImplementationListener()

    fun setListener(listener: MainActivityListener) {
        this.listener = listener
    }
}
