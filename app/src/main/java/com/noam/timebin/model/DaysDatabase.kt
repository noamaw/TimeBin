package com.noam.timebin.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Day::class], version = 2)
abstract class DaysDatabase : RoomDatabase() {

    abstract fun dayDao() : DayDao

    companion object : SingletonHolder<DaysDatabase, Context>({
        val databaseName = "days_database"
        Room.databaseBuilder(it.applicationContext, DaysDatabase::class.java, databaseName).build()
    })
}

open class SingletonHolder<T, A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}