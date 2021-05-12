package com.example.reminderkotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

class App: Application() {
    companion object{
        val CHANNEL_ID_1 = "CHANNEL_1"
        val CHANNEL_ID_2 = "CHANNEL_2"
    }

    override fun onCreate() {
        super.onCreate()
        this.createNotificationChannel()
    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel1 = NotificationChannel(
                CHANNEL_ID_1,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is channel 1"

            var channel2 = NotificationChannel(
                CHANNEL_ID_2,
                "Channel 2",
                NotificationManager.IMPORTANCE_LOW
            )
            channel2.description = "This is channel 2"

            var manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
            Log.i("IFORM","CREATE CHANNEL SUCCESS !!!")
        }
        else{
            Log.i("IFORM","CREATE CHANNEL FAIL !!!")
        }
    }
}