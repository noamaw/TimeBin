package com.noam.timebin.model

data class MyTimer(
    val startTime: Long,
    var isCurrentlyRunning: Boolean = true,
    var stopTime: Long = startTime) {

    private val breaks : MutableList<Break> = mutableListOf()

    fun stop(stopTime: Long) {
        this.stopTime = stopTime
        isCurrentlyRunning = false
    }

    fun addBreak(startTime: Long, endTime: Long) {
        breaks.add(Break(startTime, endTime))
        isCurrentlyRunning = true
        stopTime = endTime
    }

    fun isDummy() : Boolean { return startTime == 0L }

    fun calculateTimePassed() : Long {
        var totalTimePassed = when {
            isDummy() -> { 0L }
            isCurrentlyRunning -> { System.currentTimeMillis() - startTime }
            else -> { stopTime - startTime }
        }
        breaks.forEach { totalTimePassed -= (it.endTime - it.startTime) }
        return totalTimePassed
    }

    companion object {
        fun createDummy() : MyTimer {
            return MyTimer(0L, false)
        }
    }

    data class Break(val startTime: Long, val endTime: Long)
}