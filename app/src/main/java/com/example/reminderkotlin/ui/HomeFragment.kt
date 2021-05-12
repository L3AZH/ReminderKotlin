package com.example.reminderkotlin.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderkotlin.MainActivity
import com.example.reminderkotlin.R
import com.example.reminderkotlin.TaskViewModel
import com.example.reminderkotlin.adapter.TaskAdapter
import com.example.reminderkotlin.databinding.FragmentHomeBinding
import com.example.reminderkotlin.model.Task
import com.example.reminderkotlin.receiver.AlertReceiver
import com.example.reminderkotlin.util.Constance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val TAG = "HOMEFRAGMENT"
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: TaskViewModel
    lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickFoaltButton()
        setUpRecycleView()
        setUpItemToucherHelperCallback()
        taskAdapter.setOnclickListener {
            setOnClickTask(it)
        }
    }

    fun setUpRecycleView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            taskAdapter = TaskAdapter()
            adapter = taskAdapter
        }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllTask().await().observe(viewLifecycleOwner, Observer {
                taskAdapter.differ.submitList(it)
            })
        }
    }

    fun setOnClickFoaltButton() {
        binding.floatingActionButton.setOnClickListener {
            val goToAddTaskFragment = HomeFragmentDirections.actionHomeFragmentToAddTaskFragment()
            findNavController().navigate(goToAddTaskFragment)
        }
    }

    fun setOnClickTask(task:Task){
        val goToEditTaskFragment = HomeFragmentDirections.actionHomeFragmentToEditTaskFragment(task)
        findNavController().navigate(goToEditTaskFragment)
    }

    fun setUpItemToucherHelperCallback(){
        var itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = taskAdapter.differ.currentList[position]
                Log.i(TAG, "onSwiped: ${position},${task.id},${task.requestCodePendingIntent}")
                /*
                * Để cancel alarm manager thì cần các điều kiện sau
                * context giống
                * 2 intent phải giống nhau( cách thành phần như intent action sẽ dc so sánh, inputExtra sẽ ko ảnh hưởng ts kết quả so sánh)
                * 2 pendingIntent phải có requestcode giống nhau và sử dụng PendingIntent.FLAG_UPDATE_CURRENT*/
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context,AlertReceiver::class.java)
                intent.action = task.actionIntentType
                val pendingIntent = PendingIntent.getBroadcast(context,task.requestCodePendingIntent,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)
                viewModel.deleteTask(task)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }
}