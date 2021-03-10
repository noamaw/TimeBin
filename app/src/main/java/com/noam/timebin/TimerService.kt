 package com.noam.timebin

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.noam.timebin.model.TimerData
import com.noam.timebin.utils.*
import com.noam.timebin.work_manager.MyWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

class TimerService : Service() {
    private val TAG: String = "TimerService"
    val NOTIFICATION_ID = 543
    val CHANNEL_ID = "TimeBin"
    val SET_TIME_LENGTH = 30L
    val SET_TIME_TYPE = "minutes"
    var isServiceRunning = false
    private var updateTimer = Timer()
    private var notification: NotificationCompat.Builder? = null

    private val screenStateBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val currentTime : Long
            when (intent.action) {
                Intent.ACTION_SCREEN_ON -> {
                    Log.d("TimerService", "action screen on")
                    val myKM = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    if (!myKM.isKeyguardLocked) {
                        Log.d("TimerService", "action screen not locked")
                        currentTime = System.currentTimeMillis()
                        if (TimerData.shouldAddBreak(currentTime)) {
                            TimerData.addBreak(currentTime)
                            Log.d(
                                "TimerService", "time has passed ${
                                    convertLongToFormattedTime(
                                        TimerData.getCurrentTimerTime()
                                    )
                                }"
                            )
                        }
                        scheduleTimerThread()
                    }
                }
                Intent.ACTION_SCREEN_OFF -> {
                    Log.d("TimerService", "action screen off")
                    currentTime = System.currentTimeMillis()
                    TimerData.stop(currentTime)
                    Log.d(
                        "TimerService", "time has passed ${
                            convertLongToFormattedTime(
                                    TimerData.getCurrentTimerTime()
                            )
                        }"
                    )
                    cancelTimerThread()
                }
                Intent.ACTION_USER_PRESENT -> {
                    Log.d("TimerService", "action user present")
                    TimeBinApplication.logger.log("g", "action user present")
                    currentTime = System.currentTimeMillis()
                    if (TimerData.shouldAddBreak(currentTime)) {
                        TimerData.addBreak(currentTime)
                        Log.d(
                            "TimerService", "time has passed ${
                                convertLongToFormattedTime(
                                    TimerData.getCurrentTimerTime()
                                )
                            }"
                        )
                    } else {
                        createNewTimer(currentTime)
                        Log.d("TimerService", "creating new timer")
                    }
                    scheduleTimerThread()
                }
                Intent.ACTION_USER_UNLOCKED -> {
                    Log.d("TimerService", "action user unlocked")
                }
                ACTION_REQUEST_TIME_PASSED -> {
                    sendMessageToActivity()
                }
            }
        }

    }

    private fun createNewTimer(currentTime: Long) {
        TimerData.createNewTimer(currentTime)
    }

    override fun onCreate() {
        super.onCreate()
        val handlerThread = HandlerThread("DifferentThread", Process.THREAD_PRIORITY_BACKGROUND)
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        // Register the broadcast receiver to run on the separate Thread
        val screenStateFilter = IntentFilter()
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF)
        screenStateFilter.addAction(Intent.ACTION_USER_UNLOCKED)
        screenStateFilter.addAction(Intent.ACTION_USER_PRESENT)
        screenStateFilter.addAction(ACTION_REQUEST_TIME_PASSED)
        registerReceiver(screenStateBroadcastReceiver, screenStateFilter,null, handler)
        scheduleEndOfDayWork()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TimerService", "onStartCommand called")
        val r = Runnable {
            when {
                intent != null && intent.action == ACTION_START_SERVICE -> {
                    startServiceWithNotification()
                }
                intent != null && intent.action == ACTION_STOP_SERVICE -> {
                    stopMyService()
                }
                else -> {
                    stopMyService()
                }
            }
        }
        val t = Thread(r)
        t.start()
        return START_STICKY
    }

    // In case the service is deleted or crashes some how
    override fun onDestroy() {
        Log.d("TimerService", "onDestroy called")
        isServiceRunning = false
        TimeBinApplication.isServiceRunning = false
        unregisterReceiver(screenStateBroadcastReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Used only in case of bound services.
        return null
    }

    private fun startServiceWithNotification() {
        if (isServiceRunning) {
            // service is already running
            return
        }
        isServiceRunning = true
        TimeBinApplication.isServiceRunning = true

        val notification = createMyNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, "My Background Service")
        }
        startForeground(NOTIFICATION_ID, notification.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)
        return channelId
    }

    private fun createMyNotification() : NotificationCompat.Builder {
        val icon = BitmapFactory.decodeResource(resources, R.drawable.my_icon)
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.action = ACTION_MAIN // A string containing the action name
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(resources.getString(R.string.app_name))
            .setTicker(resources.getString(R.string.app_name))
            .setSmallIcon(R.drawable.my_icon)
            .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
            .setSmallIcon(R.drawable.my_icon)
            .setAutoCancel(false)
            .setColorized(true).setColor(resources.getColor(R.color.colorAccent, theme))
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentPendingIntent)
    }

    private fun updateNotification() {
        if (notification == null) {
            notification = createMyNotification()
        }
        notification?.setContentText(
            "${resources.getString(R.string.my_string)}  ${
                convertLongToFormattedTime(
                    TimerData.getCurrentTimerTime()
                )
            }"
        )
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID, notification?.build())
    }

    private fun stopMyService() {
        TimerData.stop(System.currentTimeMillis())
        cancelTimerThread()
        stopForeground(true)
        stopSelf()
        isServiceRunning = false
        TimeBinApplication.isServiceRunning = false
    }

    private fun sendMessageToActivity() {
        if (TimeBinApplication.isActivityRunning) {
            val intent = Intent(ACTION_SERVICE_TO_ACTIVITY)
            // You can also include some extra data.
            intent.putExtra(EXTRA_TIME_PASSED, TimerData.getCurrentTimerTime())
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        }
    }

    fun scheduleTimerThread() {
        updateTimer = Timer()
        updateTimer.schedule(object : TimerTask() {
            override fun run() {
                updateNotification()
                sendMessageToActivity()
            }
        }, 0, 1000) // here 1000 means 1000 mills i.e. 1 second
        setAlertForUser()
    }

    private fun setAlertForUser() {
        updateTimer.schedule(object : TimerTask() {
            override fun run() {
                if (getUserSetTimeLength() - TimerData.getCurrentTimerTime() > 3) {
                    setAlertForUser()
                } else {
                    alertUser()
                }
            }
        }, getNextAlertDelay())
    }

    private fun getNextAlertDelay() : Long {
        val delay = getUserSetTimeLength() - TimerData.getCurrentTimerTime()
        return when {
             delay > 0 -> {
                delay
            }
            delay < 0 -> {
                1000 * 60
            }
            else -> delay
        }
    }

    private fun getUserSetTimeLength(): Long {
        return 1000 * 60 * SET_TIME_LENGTH
    }

    fun cancelTimerThread() {
        updateTimer.cancel()
    }

    private fun alertUser() {
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = createMyNotification()
            .setContentText("YOU'VE BEEN USING YOUR PHONE FOR $SET_TIME_LENGTH $SET_TIME_TYPE")
            .setAutoCancel(true)
            .setColorized(true)
            .setColor(resources.getColor(android.R.color.holo_red_light, theme))
            .setSound(alarmSound)
            .build()

        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID + 2, notification)
    }

    private fun scheduleEndOfDayWork() {
        val today: LocalDateTime = LocalDateTime.now()
        val tomorrow = today.with(LocalTime.MAX).plusMinutes(1)
        val delay: Long = Duration.between(today, tomorrow).toMinutes()
        Log.d(TAG, "scheduling end of day work for another $delay minutes til it is $tomorrow.")

        val workRequest = PeriodicWorkRequest.Builder(
                MyWorker::class.java,
                24,
                TimeUnit.HOURS,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
        )
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .addTag("send_reminder_periodic")
                .build()


        WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(
                        "send_reminder_periodic",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        workRequest
                )
    }
}