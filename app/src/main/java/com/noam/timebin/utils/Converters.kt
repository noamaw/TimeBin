package com.noam.timebin.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noam.timebin.model.MyTimer
import java.lang.reflect.Type
import java.time.LocalDate

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun fromTimersList(timers: List<MyTimer?>?): String? {
        if (timers == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<MyTimer?>?>() {}.type
        return gson.toJson(timers, type)
    }

    @TypeConverter
    fun toMyTimerList(myTimerString: String?): List<MyTimer?>? {
        if (myTimerString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<MyTimer?>?>() {}.type
        return gson.fromJson(myTimerString, type)
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun dateToTimestamp(date: LocalDate?): Long? {
            return date?.toEpochDay()
        }
    }
}