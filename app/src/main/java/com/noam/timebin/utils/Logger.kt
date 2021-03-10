package com.noam.timebin.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class Logger(context: Context) {
    private val TAG = this.javaClass.name
    var logFile : File
    private val appDirectory: File =  File(context.getExternalFilesDir(null), "logs")
    private val logDirectory: File

    init {
        logDirectory = File("$appDirectory")
        logFile = File(logDirectory, "logcat_" + System.currentTimeMillis() + ".txt")
    }

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /* Checks if external storage is available to at least read */
    private fun isExternalStorageReadable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state ||
                Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    fun createFile() {
        val direct = logDirectory
        Log.d(TAG, "get the direct " + direct.absoluteFile)
        if (!direct.exists()) {
            direct.mkdir()
            Log.d(TAG, "creating the dir")
        }

        val fileNameTimeStamp = "${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())}"

        val fileName = "$fileNameTimeStamp.html"
        val file = File(direct.absolutePath + File.separator + fileName)

        if (!file.exists()) {
            if (isExternalStorageWritable()) {
                try {
                    if (!file.createNewFile()) {
                        println("File already exists")
                    }
                } catch (ex: IOException) {
                    println(ex)
                }
            } else {
                Log.d(TAG, "externbal storage not writable")
            }
        }
        Log.d(TAG, "created the file " + file.absolutePath)
        if (file.exists()) {
            logFile = file
        }
    }

    fun log(tag: String?, message: String) {

        try {
            val logTimeStamp = SimpleDateFormat(
                "E MMM dd yyyy 'at' hh:mm:ss:SSS aaa",
                Locale.getDefault()
            ).format(Date())

            if (logFile.exists()) {
                Log.d(TAG, "appending to the file")
                val fileOutputStream = FileOutputStream(logFile, true)
                //Here I have added a html tag to beautify/highlight the output in file.
                fileOutputStream.write("<p style=\"background:lightgray;\"><strong style=\"background:lightblue;\">&nbsp&nbsp$logTimeStamp :&nbsp&nbsp</strong>&nbsp&nbsp$message</p>".toByteArray())
                fileOutputStream.close()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error while logging into file : $e")
        }

    }


}