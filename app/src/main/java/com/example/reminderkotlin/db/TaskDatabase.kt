package com.example.reminderkotlin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reminderkotlin.model.Task


@Database(entities = [Task::class],version = 1)
abstract class TaskDatabase :RoomDatabase(){
    abstract fun getTaskDao():TaskDao
    companion object{
        @Volatile
        private var instance:TaskDatabase?=null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?:createDatabse(context).also {
                    instance = it
            }
        }
        private fun createDatabse(context: Context):TaskDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task_database.db"
            ).build()
        }
    }
}