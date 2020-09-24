package com.noam.timebin.utils

import java.text.SimpleDateFormat
import java.util.*


fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("hh:mm:ss", Locale.ENGLISH)
    return format.format(date)
}

fun convertLongToFormattedTime(timePassed: Long): String {
    val hours = (timePassed / (1000 * 60 * 60)).toInt()
    val minutes = (timePassed / (1000 * 60) % 60).toInt()
    val seconds = (timePassed / (1000) % 60).toInt()
    return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
}

fun runningTimerThread(timePassed : Long) {
    val updateTimer = Timer()
    updateTimer.schedule(object : TimerTask() {
        override fun run() {
            try {
//                val mills: Long = date1.getTime() - date2.getTime()
//                val hours: Int = millis / (1000 * 60 * 60)
//                val mins = (mills / (1000 * 60) % 60).toInt()
//                val diff =
//                    "$hours:$mins" // updated value every1 second
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }, 0, 1000) // here 1000 means 1000 mills i.e. 1 second

}