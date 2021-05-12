package com.example.reminderkotlin.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlin.IllegalStateException

class AlerDialog:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("app may be not working well if battery saved was on ")
                .setNegativeButton("OK",DialogInterface.OnClickListener {dialog,id->
                    dialog?.cancel()
                })
            builder.create()
        }?:throw IllegalStateException("Activity must not be null")
    }
}