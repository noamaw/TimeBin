package com.noam.timebin.model

enum class TimeType(val multiplier : Long) {
    SECONDS(1000), MINUTES(1000*60), HOURS(1000*60*60)
}