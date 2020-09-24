package com.noam.timebin.model

import androidx.room.*

@Dao
interface DayDao {
    @Insert
    fun insert(day : Day)
    @Update
    fun update(day: Day)
    @Delete
    fun delete(day: Day)

    @Query( "SELECT * FROM days WHERE total_time_spent > :time")
    fun getDaysWithHighTime(time : Int) : List<Day>

}