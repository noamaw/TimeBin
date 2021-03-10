package com.noam.timebin.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.noam.timebin.utils.*
import java.time.LocalDate

@Entity(tableName = TABLE_ALL_DAYS_LIST)
@TypeConverters(Converters::class)
data class Day @RequiresApi(Build.VERSION_CODES.O) constructor(@PrimaryKey @ColumnInfo(name = DATE_ID) val date: LocalDate,
                                                               @ColumnInfo(name = TIMERS_COLUMN) val timers : List<MyTimer>,
                                                               @ColumnInfo(name = TOTAL_TIME_COLUMN) var totalTimeSpent : Int = 0) {

    init {
        totalTimeSpent = calculateTimeOnPhone().toInt()
    }

    private fun calculateTimeOnPhone(): Long {
        var timeSpentOnPhone = 0L
        timers.forEach {
            timeSpentOnPhone += it.calculateTimePassed()
        }
        return timeSpentOnPhone
    }
}