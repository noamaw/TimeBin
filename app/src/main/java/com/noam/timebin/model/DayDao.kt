package com.noam.timebin.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.noam.timebin.utils.DATE_ID
import com.noam.timebin.utils.TABLE_ALL_DAYS_LIST
import com.noam.timebin.utils.TOTAL_TIME_COLUMN

@Dao
interface DayDao {
    @Insert(onConflict = REPLACE)
    fun insert(day : Day)

    @Update
    fun update(day: Day)

    @Delete
    fun delete(day: Day)

    @Query( "SELECT * FROM $TABLE_ALL_DAYS_LIST WHERE $TOTAL_TIME_COLUMN > :time")
    fun getDaysWithHighTime(time : Int) : LiveData<List<Day>>

    @Query( "SELECT * FROM $TABLE_ALL_DAYS_LIST")
    fun getAllDays() : LiveData<List<Day>>

    @Query("SELECT count(*) FROM $TABLE_ALL_DAYS_LIST")
    fun getDaysCount(): Int

    @Query("DELETE FROM $TABLE_ALL_DAYS_LIST")
    fun deleteAllDays()

    @Query("SELECT * FROM $TABLE_ALL_DAYS_LIST WHERE $DATE_ID=:date_id")
    fun getDay(date_id: Long?): LiveData<Day>

}