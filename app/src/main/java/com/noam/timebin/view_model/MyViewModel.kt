package com.noam.timebin.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.noam.timebin.model.Day
import com.noam.timebin.model.DaysRepository
import com.noam.timebin.model.MyTimer
import com.noam.timebin.model.TimerData

class MyViewModel(applicationContext: Context) : ViewModel() {
    private var daysRepository : DaysRepository = DaysRepository(applicationContext)
    private var allDays : LiveData<List<Day>>
    private var timers: MutableLiveData<List<MyTimer>>? = null
    private var runningTimer : MutableLiveData<MyTimer>? = null

    init {
        allDays = daysRepository.getAllDays()
    }

    fun getRunningTimer(): MutableLiveData<MyTimer>? {
        if (runningTimer == null) {
            runningTimer = MutableLiveData()
            loadTimer()
        }
        return runningTimer
    }

    fun getTimers(): LiveData<List<MyTimer>>? {
        if (timers == null) {
            timers = MutableLiveData<List<MyTimer>>()
            loadTimers()
        }
        return timers
    }

    private fun loadTimer() {
        runningTimer?.value = TimerData.getCurrentTimer()
    }

    private fun loadTimers() {
        // Do an asynchronous operation to fetch users.
        timers?.value = TimerData.getTimers()
    }

    fun insert(day: Day) {
        daysRepository.insert(day)
    }
}