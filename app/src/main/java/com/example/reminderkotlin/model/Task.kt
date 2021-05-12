package com.example.reminderkotlin.model

import android.app.PendingIntent
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String,
    val time:String,
    val date:String,
    val requestCodePendingIntent: Int,
    val actionIntentType:String
):Serializable