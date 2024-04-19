package com.taskdemo.practical.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taskdemo.practical.R
import com.taskdemo.practical.databinding.ItemTaskBinding
import com.taskdemo.practical.model.TaskModel
import com.taskdemo.practical.utils.OnItemClickListener
import java.text.SimpleDateFormat
import java.util.*

// first create adapter class. This inherits recycler view. Recycler view now requires view holder
class TaskAdapter(private val list: List<TaskModel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TaskAdapter.TodoViewHolder>() {
//    init {
//        val listener: OnItemClickListener = listener
//    }


    // 3 functions of the view holder
    // 1st func
    // In this Layout inflatter is called which converts view in such a form that adapter can consume it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemBinding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(itemBinding)
    }


    override fun getItemCount() = list.size

    // 2nd func
    // this will set data in each card
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val taskModel = list[position]

        holder.viewBinding.imgTaskNotification

        holder.viewBinding.txtShowTitle.text = taskModel.title
        holder.viewBinding.txtShowTask.text = taskModel.description
        holder.viewBinding.imgTaskUndo.visibility = View.GONE


        updateTime(taskModel.time, holder.viewBinding.txtShowTime)
        updateDate(taskModel.date, holder.viewBinding.txtShowDate)
        updateNotificationOnOff(taskModel.isNotification, holder.viewBinding.imgTaskNotification)

        holder.viewBinding.imgTaskDelete.setOnClickListener {
            listener.onItemClick(
                taskModel,
                holder.viewBinding.imgTaskDelete
            )
        }

        holder.viewBinding.imgTaskDone.setOnClickListener {
            listener.onItemClick(
                taskModel,
                holder.viewBinding.imgTaskDone
            )
        }
        holder.viewBinding.imgTaskEdit.setOnClickListener {
            listener.onItemClick(
                taskModel,
                holder.viewBinding.imgTaskEdit
            )
        }
        holder.viewBinding.imgTaskNotification.setOnClickListener {
            listener.onItemClick(
                taskModel,
                holder.viewBinding.imgTaskNotification
            )
        }
        holder.viewBinding.bgCardSelected.setOnClickListener {
            listener.onItemClick(
                taskModel,
                holder.viewBinding.bgCardSelected
            )
        }
    }

    // 3rd func
    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    inner class TodoViewHolder(var viewBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    private fun updateTime(time: Long, textview: TextView) {
        //Fri, 19 APR 2024 > 3:00PM
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        textview.text = sdf.format(Date(time))

    }

    private fun updateDate(time: Long, textview: TextView) {
        //Fri, 19 APR 2024
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        textview.text = sdf.format(Date(time))

    }

    private fun updateNotificationOnOff(isNotification: Int, imageView: ImageView) {

        if (isNotification == 0) {
            imageView.setImageResource(R.drawable.vs_ic_notification_off)
        } else {
            imageView.setImageResource(R.drawable.vs_ic_notification_on)
        }


    }
}


