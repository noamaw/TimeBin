package com.noam.timebin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "days")
data class Day(@PrimaryKey private val date: Date, val timers : MutableList<MyTimer>, @ColumnInfo(name = "total_time_spent") val totalTimeSpent : Int) {

    fun calculateTimeOnPhone(): Long {
        var timeSpentOnPhone = 0L
        timers.forEach {
            timeSpentOnPhone += it.calculateTimePassed()
        }
        return timeSpentOnPhone
    }
}