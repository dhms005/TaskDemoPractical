package com.taskdemo.practical.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.taskdemo.practical.R
import com.taskdemo.practical.adapter.TaskFinishedAdapter
import com.taskdemo.practical.databinding.ActivityFinishedTaskBinding
import com.taskdemo.practical.db.AppDatabase
import com.taskdemo.practical.model.TaskModel
import com.taskdemo.practical.utils.OnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FinishedTaskActivity : AppCompatActivity() {

    private val list = arrayListOf<TaskModel>()
    private lateinit var taskAdapter: TaskFinishedAdapter

    private lateinit var binding: ActivityFinishedTaskBinding

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_finished_task)

        setSupportActionBar(binding.toolbar)

        taskAdapter = TaskFinishedAdapter(list, object : OnItemClickListener {
            override fun onItemClick(taskModel: TaskModel, view: View?) {
//                TODO("Not yet implemented")

                when (view?.id) {
                    R.id.imgTaskDelete -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.todoDao().deleteTask(taskModel.id)
                        }
                    }
                    R.id.imgTaskUndo -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.todoDao().unFinishTask(taskModel.id)
                        }
                    }
                }
            }

        })
        binding.todoRv.apply {
            layoutManager = LinearLayoutManager(this@FinishedTaskActivity)
            adapter = this@FinishedTaskActivity.taskAdapter
        }

        db.todoDao().getFinishedTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                taskAdapter.notifyDataSetChanged()
            } else {
                list.clear()
                taskAdapter.notifyDataSetChanged()
            }
        })
    }
}
