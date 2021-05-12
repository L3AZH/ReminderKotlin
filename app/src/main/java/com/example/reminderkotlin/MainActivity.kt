package com.example.reminderkotlin

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.reminderkotlin.databinding.ActivityMainBinding
import com.example.reminderkotlin.db.TaskDatabase
import com.example.reminderkotlin.dialog.AlerDialog
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel:TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = TaskDatabase(this).getTaskDao()
        val repository = Repository(dao)
        val taskViewModelFactory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this,taskViewModelFactory).get(TaskViewModel::class.java)
        binding.lifecycleOwner = this
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS),1)
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_BOOT_COMPLETED),2)
        }
        val model = android.os.Build.MODEL
        val manufacture = android.os.Build.MANUFACTURER
        Snackbar.make(binding.root,model+" - "+manufacture,Snackbar.LENGTH_LONG).show()
        if(manufacture.contains("Google")){
            val dialog = AlerDialog()
            dialog.show(supportFragmentManager, "MyAlerDialog")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size>0){
                Snackbar.make(binding.root,"Access ignore pin granted",Snackbar.LENGTH_LONG)
            }
        }
        if(requestCode==0){
            if(grantResults.size>0){
                Snackbar.make(binding.root,"Recieve Boot Completed granted",Snackbar.LENGTH_LONG)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}