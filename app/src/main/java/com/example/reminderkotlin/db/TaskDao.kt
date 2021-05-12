package com.example.reminderkotlin.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reminderkotlin.model.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)
    @Delete
    suspend fun detele(task: Task)
    @Update
    suspend fun update(task: Task)
    @Query("select * from task_table")
    fun getAllTask():LiveData<List<Task>>
}