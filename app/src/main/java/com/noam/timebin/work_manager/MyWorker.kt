package com.noam.timebin.work_manager

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.noam.timebin.TimeBinApplication

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Do the work here
        Log.d("MyWorker", "doing the work of the end of the day")
        TimeBinApplication.logger.createFile()
        TimeBinApplication.logger.log("", "starting end of day work")
        val endDayWork = EndDayWork(applicationContext)
        TimeBinApplication.logger.log("", "doing end of day work")
        endDayWork.doEndOfDayWork()
        TimeBinApplication.logger.log("", "finished end of day work")
        // Indicate success or failure with your return value:
        return Result.success()

        // (Returning RETRY tells WorkManager to try this task again
        // later; FAILURE says not to try again.)
    }

}