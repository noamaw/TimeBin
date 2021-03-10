 package com.noam.timebin.model

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class DaysRepository(applicationContext: Context) {
    private var daysDao: DayDao
    private var allDays: LiveData<List<Day>>
    private var todayTimers : MutableLiveData<MutableList<MyTimer>>
    private var runningTimer : MutableLiveData<MyTimer>

    init {
        val db: DaysDatabase = DaysDatabase.getInstance(applicationContext)
        daysDao = db.dayDao()
        allDays = daysDao.getAllDays()
        todayTimers = MutableLiveData<MutableList<MyTimer>>()
        runningTimer = MutableLiveData<MyTimer>()
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun requestRunningTimer() : LiveData<MyTimer> {
        runningTimer.value = TimerData.getCurrentTimer()
        return runningTimer
    }

    fun addNewTimer(newTimer: MyTimer) {
        todayTimers.value?.add(newTimer)
        todayTimers.notifyObserver()
    }

    fun getAllDays(): LiveData<List<Day>> {
        return allDays
    }

    fun insert(day: Day) {
        InsertAsyncTask(daysDao).execute(day)
    }

    private class InsertAsyncTask internal constructor(dao: DayDao) : AsyncTask<Day?, Void?, Void?>() {
        private val mAsyncTaskDao: DayDao = dao
        override fun doInBackground(vararg params: Day?): Void? {
            params[0]?.let { mAsyncTaskDao.insert(it) }
            return null
        }

    }
}