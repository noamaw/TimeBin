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
            timer.finish()
            timers.add(timer)
        }

        fun getTimersTotalTime(): Long {
            var totalTime = 0L
            timers.forEach {
                totalTime += it.calculateTimePassed()
            }
            return totalTime
        }
    }
}
