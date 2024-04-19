package com.taskdemo.practical.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.taskdemo.practical.BaseApp
import com.taskdemo.practical.R
import com.taskdemo.practical.adapter.TaskAdapter
import com.taskdemo.practical.databinding.ActivityMainBinding
import com.taskdemo.practical.db.AppDatabase
import com.taskdemo.practical.model.TaskModel
import com.taskdemo.practical.utils.OnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val list = arrayListOf<TaskModel>()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding
    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)


        taskAdapter = TaskAdapter(list, object : OnItemClickListener {
            override fun onItemClick(taskModel: TaskModel, view: View?) {
//                TODO("Not yet implemented")

                when (view?.id) {
                    R.id.imgTaskDelete -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.todoDao().deleteTask(taskModel.id)
                        }
                    }
                    R.id.imgTaskDone -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.todoDao().finishTask(taskModel.id)
                        }
                    }
                    R.id.imgTaskEdit -> {
                        openEditTask(taskModel.id)
                    }
                    R.id.bgCardSelected -> {
                        openEditTask(taskModel.id)
                    }
                    R.id.imgTaskNotification -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            if (taskModel.isNotification == 1) {
                                db.todoDao().notificationOnOffTask(taskModel.id, 0)
                            } else {
                                db.todoDao().notificationOnOffTask(taskModel.id, 1)
                            }
                        }

                    }
                }
            }

        })
        binding.todoRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.taskAdapter
        }

        db.todoDao().getTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                taskAdapter.notifyDataSetChanged()
            } else {
                list.clear()
                taskAdapter.notifyDataSetChanged()
            }
        })


        binding.addNewTaskFab.setOnClickListener {
            openNewTask()
        }
        permission()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                displayTodo()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                displayTodo()
                return true
            }

        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    displayTodo(newText)
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun displayTodo(newText: String = "") {
        db.todoDao().getTask().observe(this, Observer {
            if (it.isNotEmpty()) {
                list.clear()
                list.addAll(
                    it.filter { todo ->
                        todo.title.contains(newText, true)
                    }
                )
                taskAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.finishedTask -> {
                startActivity(Intent(this, FinishedTaskActivity::class.java))
            }
            R.id.setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun openNewTask() {
        startActivity(Intent(this, AddTaskActivity::class.java))
    }

    fun openEditTask(id: Long) {
        val taskDetailScreen = Intent(this@MainActivity, TaskDetailsActivity::class.java)
        taskDetailScreen.putExtra("id", id)
        startActivity(taskDetailScreen)
    }

    private fun permission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkPermission(this@MainActivity)) {
            }
        } else {
        }
    }

    private fun checkPermission(context: Activity): Boolean {
        val PERMISSIONS: Array<String>
        PERMISSIONS = arrayOf<String>(
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (!hasPermissions(context, *PERMISSIONS)) {
            context.requestPermissions(PERMISSIONS, 200)
        } else {
            return true
        }
        return false
    }

    private fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                backLogic();
            } else {
//                backLogic();
            }
        }
    }

}
