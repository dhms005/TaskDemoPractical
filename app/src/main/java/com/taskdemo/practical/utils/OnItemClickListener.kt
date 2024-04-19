package com.taskdemo.practical.utils

import android.view.View
import com.taskdemo.practical.model.TaskModel

interface OnItemClickListener {
    fun onItemClick(taskModel: TaskModel, view: View?)
}