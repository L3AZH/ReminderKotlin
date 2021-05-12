package com.example.reminderkotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TaskViewModelFactory(val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java)){
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown View Model")
    }
}