package com.noam.timebin.work_manager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.noam.timebin.TimeBinApplication
import com.noam.timebin.model.Day
import com.noam.timebin.model.DaysRepository
import com.noam.timebin.model.TimerData
import java.time.LocalDate

class EndDayWork(private val applicationContext: Context) {

    fun doEndOfDayWork() {
    // if running timer is on - then
    //      1. end timer.
    //      2. add to list.
    //      3. start new timer instead.
    //  else - no running timer.
    //      1. add previous timer to list (unless it's dummy)
    //      2. new dummy timer.
    //  then either way:
    //  1. take list of timers and create day object with previous day date and said list of timers.
    //  2. insert day into database.
    //  3. then create new list of timers.
        TimeBinApplication.logger.log("EndDayWork", "starting updating database")
        val currentTime = System.currentTimeMillis()
        val date = getDate()
        val timers = TimerData.endDay(currentTime)

        val today = Day(date, timers)
        TimeBinApplication.logger.log("EndDayWork", "created Day object")

        val daysRepository = DaysRepository(applicationContext)
        TimeBinApplication.logger.log("EndDayWork", "insert day into database")
        daysRepository.insert(today)
    }

    private fun getDate(): LocalDate {
        return LocalDate.now().minusDays(1)
    }
}