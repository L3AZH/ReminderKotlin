package com.example.reminderkotlin


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderkotlin.model.Task
import com.example.reminderkotlin.receiver.AlertReceiver
import com.example.reminderkotlin.util.Constance
import com.example.reminderkotlin.util.RandomIntUtil
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel(val repository: Repository) : ViewModel() {


    fun getAllTask(): Deferred<LiveData<List<Task>>> = viewModelScope.async {
        repository.getAllTask()
    }


    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun caculateDiffTime(dateOld: String, dateNew: String): Long? {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val dateOldDate: Date = dateFormat.parse(dateOld)
            val dateNewDate: Date = dateFormat.parse(dateNew)
            val diff: Long = dateNewDate.time - dateOldDate.time
            val seconds = diff / 1000
            val minutes = diff / 60
            val hours = diff / 60
            val days = diff / 24
            Log.i("MyTime: ", "result: " + diff.toString());
            return diff
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}