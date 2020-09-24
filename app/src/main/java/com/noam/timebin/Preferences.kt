package com.noam.timebin

import android.content.Context
import com.noam.timebin.model.TimeType

class Preferences(context: Context) {
    private val USER_ALERT_TIME_LENGTH_KEY = "USER_ALERT_TIME_LENGTH"
    private val USER_ALERT_TIME_TYPE_KEY = "USER_ALERT_TIME_TYPE"

    val sharedPreferences = context.getSharedPreferences(context.resources.getString(R.string.app_name), 0)

    private var userAlertTimeLength = sharedPreferences.getInt(USER_ALERT_TIME_LENGTH_KEY, 30)
    set(value) = sharedPreferences.edit().putInt(USER_ALERT_TIME_LENGTH_KEY, value).apply()

    private var userAlertTimeType = sharedPreferences.getString(USER_ALERT_TIME_TYPE_KEY, TimeType.MINUTES.name)
        set(value) = sharedPreferences.edit().putString(USER_ALERT_TIME_TYPE_KEY, value).apply()

    fun getUserAlertTime(): Long {
        return userAlertTimeLength * TimeType.valueOf(userAlertTimeType).multiplier
    }

    fun setUserAlertTime(timeLength : Int, timeType: TimeType) {
        userAlertTimeType = timeType.name
        userAlertTimeLength = timeLength
    }
}