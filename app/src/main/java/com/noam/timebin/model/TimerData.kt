package com.noam.timebin.model

import android.util.Log
import com.noam.timebin.TimeBinApplication

object TimerData {
    private var currentTimer : MyTimer = MyTimer.createDummy()
    private var timers = mutableListOf<MyTimer>()

    private fun addToTimers(timer: MyTimer) {
        timers.add(timer.copy())
    }

    fun addBreak(currentTime: Long) {
        TimeBinApplication.logger.log("TimerData", "adding break")
        currentTimer.addBreak(currentTime)
    }

    fun stop(currentTime: Long) {
        currentTimer.stop(currentTime)
    }

    fun shouldAddBreak(currentTime: Long) : Boolean {
        val FIVE_MINUTES = 5 * 60 * 1000
        return !currentTimer.isDummy() && (currentTime - currentTimer.stopTime < FIVE_MINUTES)
    }

    fun createNewTimer(currentTime: Long) {
        TimeBinApplication.logger.log("TimerData", "creating new timer")
        if (!currentTimer.isDummy()) {
            addToTimers(currentTimer)
            Log.d("TimerData", "the timer adding is $currentTimer which started at ${currentTimer.startTime} and ended at ${currentTimer.stopTime}")
        }
        currentTimer = MyTimer(currentTime)
    }

    public fun getCurrentTimer() : MyTimer {
        return currentTimer
    }

    public fun getTimers() : MutableList<MyTimer> {
        return timers
    }

    fun getCurrentTimerTime() : Long {
        return currentTimer.calculateTimePassed()
    }

    fun getTimersTotalTime(): Long {
        var totalTime = 0L
        timers.forEach {
            totalTime += it.calculateTimePassed()
        }
        return totalTime
    }

    fun endDay(currentTime: Long) : MutableList<MyTimer> {
        // if running timer is on - then
        //      1. end timer.
        //      2. add to list.
        //      3. start new timer instead.
        //  else - no running timer.
        //      1. add previous timer to list (unless it's dummy)
        //      2. new dummy timer.
        if (currentTimer.isCurrentlyRunning) {
            currentTimer.stop(currentTime)
            createNewTimer(currentTime)
        } else {
            createNewTimer(currentTime)
            currentTimer = MyTimer.createDummy()
        }
        return timers
    }
}
