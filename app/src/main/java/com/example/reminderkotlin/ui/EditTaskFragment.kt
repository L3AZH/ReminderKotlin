package com.example.reminderkotlin.ui

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.reminderkotlin.R
import com.example.reminderkotlin.databinding.FragmentEditTaskBinding
import com.example.reminderkotlin.model.Task
import com.example.reminderkotlin.util.Constance

class EditTaskFragment : Fragment() {

    lateinit var  binding:FragmentEditTaskBinding
    val args:EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_task,container,false)
        binding.task = args.task
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwitchChanged()
        setSelectItemSpinner(args.task)
        setonclickevent()

    }

    fun setSelectItemSpinner(task:Task){
        when(task.actionIntentType){
            Constance.ACTION_SET_EXACT_ALARM->binding.spinner.setSelection(0)
            Constance.ACTION_SET_REPETITIVE_ALARM_WEEK->binding.spinner.setSelection(1)
            Constance.ACTION_SET_REPETITIVE_ALARM_MONTH->binding.spinner.setSelection(2)
        }
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
        binding.timeSwitch.isChecked = true
        binding.dateSwitch.isChecked = true
        binding.repeatSwitch.isChecked = true
    }

    fun setonclickevent(){
        binding.cancelBtn.setOnClickListener {
            val goToHome = EditTaskFragmentDirections.actionEditTaskFragmentToHomeFragment()
            findNavController().navigate(goToHome)
        }
    }
}