package com.example.reminderkotlin.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.reminderkotlin.util.Constance
import com.example.reminderkotlin.util.RandomIntUtil


class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?


    fun setExtractAlarm(timesInMillis: Long) {
        setAlarm(
            timesInMillis,
            getPendingItent(
                getIntent().apply {
                    action = Constance.ACTION_SET_EXACT_ALARM
                    putExtra(Constance.EXTRA_EXACT_ALARM_TIME,timesInMillis)
                }
            )
        )
    }

    //Every Week
    fun setRepectitiveAlarms(timesInMillis: Long) {
        setAlarm(
            timesInMillis,
            getPendingItent(
                getIntent().apply {
                    action = Constance.ACTION_SET_REPETITIVE_ALARM_WEEK
                    putExtra(Constance.EXTRA_EXACT_ALARM_TIME,timesInMillis)
                }
            )
        )
    }

    private fun setAlarm(timesInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timesInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timesInMillis, pendingIntent)
            }
        }
    }

    private fun getIntent() = Intent(context, AlarmService::class.java)

    private fun getPendingItent(intent: Intent) = PendingIntent.getBroadcast(
        context,
        RandomIntUtil.getRandom(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}