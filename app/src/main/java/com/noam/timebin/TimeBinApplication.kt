package com.noam.timebin

import android.app.Application
import com.noam.timebin.model.MyTimer
import com.noam.timebin.model.TimerData
import com.noam.timebin.utils.Logger

class TimeBinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        logger = Logger(applicationContext)
        logger.createFile()
    }

    companion object {
        lateinit var logger: Logger
        lateinit var preferences : Preferences
        var isServiceRunning : Boolean = false
        var isActivityRunning : Boolean = false
    }
}
