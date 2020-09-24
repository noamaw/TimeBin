package com.noam.timebin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private var timers: MutableLiveData<List<MyTimer>>? = null

    fun getTimers(): LiveData<List<MyTimer>>? {
        if (timers == null) {
            timers = MutableLiveData<List<MyTimer>>()
            loadTimers()
        }
        return timers
    }

    private fun loadTimers() {
        // Do an asynchronous operation to fetch users.
    }
}