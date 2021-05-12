package com.example.reminderkotlin.ui

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reminderkotlin.MainActivity
import com.example.reminderkotlin.R
import com.example.reminderkotlin.TaskViewModel
import com.example.reminderkotlin.databinding.FragmentAddTaskBinding
import com.example.reminderkotlin.model.Task
import com.example.reminderkotlin.receiver.AlertReceiver
import com.example.reminderkotlin.util.Constance
import com.example.reminderkotlin.util.RandomIntUtil
import java.util.*

class AddTaskFragment : Fragment() {

    lateinit var binding: FragmentAddTaskBinding
    lateinit var viewModel: TaskViewModel
    var myHour = 0
    var myMinute = 0
    val calendar = Calendar.getInstance()
    var myDay = calendar.get(Calendar.DAY_OF_MONTH)
    var myMonth = calendar.get(Calendar.MONTH)
    var myYear = calendar.get(Calendar.YEAR)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timeSetText.text = DateFormat.format("HH:mm ", calendar)
        binding.dateSetText.text = DateFormat.format("dd/MM/yyyy ", calendar)
        setSwitchChanged()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setUpTimePickerAndDatePicker()
        }
        setOnclickSaveBtn()
    }

    fun setSwitchChanged() {
        binding.timeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                binding.timeSetText.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                binding.timeSetText.visibility = View.GONE
            }
        }
        binding.dateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(binding.cardView2, AutoTransition())
                binding.dateSetText.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(binding.cardView2, AutoTransition())
                binding.dateSetText.visibility = View.GONE
            }
        }
        binding.repeatSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(binding.cardView3, AutoTransition())
                binding.spinner.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(binding.cardView3, AutoTransition())
                binding.spinner.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setUpTimePickerAndDatePicker() {
        binding.timeSetText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                activity,
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        myHour = hourOfDay
                        myMinute = minute
                        val calendar = Calendar.getInstance()
                        calendar.set(0, 0, 0, hourOfDay, minute)
                        binding.timeSetText.text = DateFormat.format("HH:mm ", calendar)
                    }
                },
                24,
                0,
                true
            )
            timePickerDialog.updateTime(myHour, myMinute)
            timePickerDialog.show()
        }
        binding.dateSetText.setOnClickListener {
            val dataPickerDialog = DatePickerDialog(
                requireContext(),
                object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        val date = "${dayOfMonth}/${month + 1}/${year}"
                        binding.dateSetText.text = date
                    }
                },
                myYear,
                myMonth,
                myDay
            )
            dataPickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dataPickerDialog.show()
        }
    }

    fun setOnclickSaveBtn() {
        binding.saveBtn.setOnClickListener {
            if ((binding.titleEditText.text.isNullOrBlank() || binding.titleEditText.text.isNullOrEmpty()) &&
                (!binding.decriptionEditText.text.isNullOrBlank() && !binding.decriptionEditText.text.isNullOrEmpty())
            ) {
                binding.errorForTitle.text = "please input title"
                binding.errorForTitle.visibility = View.VISIBLE
                binding.errorForDecription.visibility = View.GONE
            } else if ((binding.decriptionEditText.text.isNullOrBlank() || binding.decriptionEditText.text.isNullOrEmpty()) &&
                (!binding.titleEditText.text.isNullOrBlank() && !binding.titleEditText.text.isNullOrEmpty())
            ) {
                binding.errorForDecription.text = "please input decription"
                binding.errorForDecription.visibility = View.VISIBLE
                binding.errorForTitle.visibility = View.GONE
            } else if ((binding.decriptionEditText.text.isNullOrBlank() || binding.decriptionEditText.text.isNullOrEmpty()) &&
                (binding.titleEditText.text.isNullOrBlank() || binding.titleEditText.text.isNullOrEmpty())
            ) {
                binding.errorForTitle.text = "please input title"
                binding.errorForDecription.text = "please input decription"
                binding.errorForDecription.visibility = View.VISIBLE
                binding.errorForTitle.visibility = View.VISIBLE
            } else {
                binding.errorForTitle.visibility = View.GONE
                binding.errorForDecription.visibility = View.GONE
                val currentDay =
                    DateFormat.format("dd/MM/yyyy HH:mm", Calendar.getInstance().time).toString()
                val setDay =
                    binding.dateSetText.text.toString() + " " + binding.timeSetText.text.toString()
                Log.i("MyTime", "currentDay " + currentDay + " -------Set Day: " + setDay)
                val diff = viewModel.caculateDiffTime(currentDay, setDay)
                val alarmManager =
                    requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, AlertReceiver::class.java)
                when (binding.spinner.selectedItem.toString()) {
                    "Daily" -> intent.action = Constance.ACTION_SET_EXACT_ALARM
                    "Weekly" -> intent.action = Constance.ACTION_SET_REPETITIVE_ALARM_WEEK
                    "Monthly" -> intent.action = Constance.ACTION_SET_REPETITIVE_ALARM_MONTH
                    else -> intent.action = Constance.ACTION_SET_EXACT_ALARM
                }
                val actionIntentType = intent.action
                intent.putExtra(Constance.EXTRA_TITLE, binding.titleEditText.text.toString())
                intent.putExtra(
                    Constance.EXTRA_DECRIPTION,
                    binding.decriptionEditText.text.toString()
                )
                val getRequestCodePendingIntent = RandomIntUtil.getRandom()
                intent.putExtra(Constance.EXTRA_REQUESTCODE_PENDING, getRequestCodePendingIntent)
                /*
                * pendingrequestcode phải khác nhau để định danh các báo thức ko là sẽ bị ghi chồng báo thức
                * */
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    getRequestCodePendingIntent,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                /*
                *  believe the error is here:

        alarm.SetExact(int type, long triggerAtMillis, PendingIntent operation);
        triggerAtMillis: time in milliseconds that the alarm should go off, using the appropriate clock (depending on the alarm type).

        So, your are using 1800000 as triggerAtMillis. However, 1800000 is following date in UTC: Thu Jan 01 1970 00:30:00

        Since this is an old date, the alarm is fired immediately.

        Solution

        Maybe, you should update your code as follows:

        In MainActivity, I believe that you want to fire the alarm immediately. So, create it as follows:

        alarm.SetExact(AlarmType.RtcWakeup, Calendar.getInstance().getTimeInMillis(), pending);
        In your service, it seems that you want to trigger your alarm after 1800000. So, you have to use:

        alarm.SetExact(AlarmType.RtcWakeup, Calendar.getInstance().getTimeInMillis() + LOCATION_INTERVAL, pending);
        This way, alarm will be fired 30 minutes after current time (current time + LOCATION_INTERVAL).

        Keep in mind that second parameter is the date in milliseconds... It is a number which represents an whole date (and not only an interval)...
                * */
                alarmManager?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // sử dụng dozen mode, google android doc => báo khi đã bật tiết kiệm pin
                        it.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            Calendar.getInstance().timeInMillis + diff!!,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            Calendar.getInstance().timeInMillis + diff!!,
                            pendingIntent
                        )
                    }
                }
                viewModel.insertTask(
                    Task(
                        0,
                        binding.titleEditText.text.toString(),
                        binding.decriptionEditText.text.toString(),
                        binding.timeSetText.text.toString(),
                        binding.dateSetText.text.toString(),
                        getRequestCodePendingIntent,
                        actionIntentType!!
                    )
                )
                val goToHomeFragment =
                    AddTaskFragmentDirections.actionAddTaskFragmentToHomeFragment()
                findNavController().navigate(goToHomeFragment)
            }
        }
    }

}