package com.noam.timebin.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Day::class), version = 2)
abstract class DaysDatabase : RoomDatabase() {

    abstract fun dayDao() : DayDao
}