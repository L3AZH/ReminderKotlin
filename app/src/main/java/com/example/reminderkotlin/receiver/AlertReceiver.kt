package com.example.reminderkotlin.receiver

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.service.quicksettings.Tile
import android.util.Log
import android.util.TimeUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminderkotlin.App
import com.example.reminderkotlin.MainActivity
import com.example.reminderkotlin.R
import com.example.reminderkotlin.util.Constance
import com.example.reminderkotlin.util.RandomIntUtil
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit

class AlertReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent!!.getStringExtra(Constance.EXTRA_TITLE)
        val decription = intent!!.getStringExtra(Constance.EXTRA_DECRIPTION)
        val requestCodePending = intent!!.getIntExtra(Constance.EXTRA_REQUESTCODE_PENDING,0)
        Log.i("requestCode",requestCodePending.toString())
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        val pendingIntent = PendingIntent.getActivity(context,0,activityIntent,0)
        val notification = NotificationCompat.Builder(context!!, App.CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_access_time_filled_24)
            .setContentTitle(title)
            .setContentText(decription)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setColor(Color.MAGENTA)
            .build()
        val ID_noti = RandomIntUtil.getRandom()
        with(NotificationManagerCompat.from(context)){
            notify(ID_noti,notification)
        }
        when(intent.action){
            Constance.ACTION_SET_EXACT_ALARM->{
                setUpAlarm(context,1,title!!,decription!!,requestCodePending,Constance.ACTION_SET_EXACT_ALARM)
            }
            Constance.ACTION_SET_REPETITIVE_ALARM_WEEK->{
                setUpAlarm(context,7,title!!,decription!!,requestCodePending,Constance.ACTION_SET_REPETITIVE_ALARM_WEEK)
            }
            Constance.ACTION_SET_REPETITIVE_ALARM_MONTH->{
                setUpAlarm(context,30,title!!,decription!!,requestCodePending,Constance.ACTION_SET_REPETITIVE_ALARM_MONTH)
            }
        }
    }
    fun setUpAlarm(context: Context,repeat:Long,title: String,decription:String,requestCode:Int,flag:String){
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        intent.action = flag
        intent.putExtra(Constance.EXTRA_TITLE,title)
        intent.putExtra(Constance.EXTRA_DECRIPTION,decription)
        intent.putExtra(Constance.EXTRA_REQUESTCODE_PENDING,requestCode)
        Log.i("mytime", "setUpAlarm: ${Calendar.getInstance().time },${TimeUnit.DAYS.toMillis(repeat)}")
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(repeat),
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(repeat),
                    pendingIntent
                )
            }
        }
    }
}