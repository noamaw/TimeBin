package com.noam.timebin

import android.app.Application
import com.noam.timebin.model.MyTimer

class TimeBinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
    }

    companion object {
        lateinit var preferences : Preferences
        var timers = mutableListOf<MyTimer>()
        var isServiceRunning : Boolean = false
        var isActivityRunning : Boolean = false

        fun addToTimers(timer: MyTimer) {
            timers.add(timer)
        }

    }
}
