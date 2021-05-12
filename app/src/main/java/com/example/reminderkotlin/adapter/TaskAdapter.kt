package com.example.reminderkotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderkotlin.R
import com.example.reminderkotlin.databinding.ActivityMainBinding
import com.example.reminderkotlin.databinding.ItemReviewLayoutBinding
import com.example.reminderkotlin.model.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {


    private var onClickListener: ((Task) -> Unit)? = null
    fun setOnclickListener(listener: (Task) -> Unit) {
        this.onClickListener = listener
    }

    var differCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemReviewLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_review_layout, parent, false)
        return MyViewHolder((binding))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding(differ.currentList[position],onClickListener)
    }

    inner class MyViewHolder(val binding: ItemReviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(task: Task, listener: ((Task) -> Unit)?) {
            binding.apply {
                tilte.text = task.title
                decreption.text = task.description
                date.text = task.date
                time.text = task.time
            }
            itemView.setOnClickListener {
                listener?.let {
                    it(task)
                }
            }
        }
    }
}