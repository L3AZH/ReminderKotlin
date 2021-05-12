package com.example.reminderkotlin

import androidx.lifecycle.LiveData
import com.example.reminderkotlin.db.TaskDao
import com.example.reminderkotlin.model.Task

class Repository(val dao: TaskDao) {
    suspend fun insertTask(task:Task){
        dao.insert(task)
    }
    suspend fun deleteTask(task:Task){
        dao.detele(task)
    }
    suspend fun updateTask(task:Task){
        dao.insert(task)
    }
    suspend fun getAllTask():LiveData<List<Task>>{
        return dao.getAllTask()
    }
}