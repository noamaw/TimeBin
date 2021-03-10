package com.noam.timebin.model

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noam.timebin.utils.Converters
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DaysDatabaseTest {
    private val TAG = "DaysDatabaseTest"
    private lateinit var daysDao: DayDao
    private lateinit var db: DaysDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        Log.d(TAG, "createDb: starting by creating database ")
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DaysDatabase::class.java).build()
        daysDao = db.dayDao()
    }

    @Test
    fun should_Insert_Day_Item() {
        Log.d(TAG, "should_Insert_Day_Item: testing insert day item")
        val date = LocalDate.of(2021, 3, 1)
        val timers = createTimers()
        val day = Day(date, timers)
        daysDao.insert(day)
        val dayTest = getValue(daysDao.getDay(Converters.dateToTimestamp(day.date)))
        Assert.assertEquals(day.date, dayTest.date)
    }

    @Test
    fun should_Flush_All_Data(){
        daysDao.deleteAllDays()
        Assert.assertEquals(daysDao.getDaysCount(), 0)
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)//To change body of created functions use File | Settings | File Templates.
            }

        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        Log.d(TAG, "closeDb: closing database")
//        db.close()
    }

    private fun createTimers() : MutableList<MyTimer> {
        val timers = mutableListOf<MyTimer>()
        val timer = MyTimer(1614602470567, false, 1614602820508)
        timers.add(timer)
        return timers
    }
}