package com.taskdemo.practical.utils.services

import android.app.IntentService
import android.content.Intent
import com.taskdemo.practical.utils.NotificationHelper
import android.widget.Toast
import com.taskdemo.practical.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Dhms on 19/4/24.
 */
class TodoDoneIntentService : IntentService(TodoDoneIntentService::class.java.simpleName) {

    private val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val id = intent!!.getLongExtra("id", 0)

        GlobalScope.launch(Dispatchers.IO) {
            db.todoDao().finishTask(id)
        }
        NotificationHelper(this@TodoDoneIntentService).dismissNotification(id.toInt())
        Toast.makeText(this@TodoDoneIntentService, "Todo Done.", Toast.LENGTH_SHORT).show()
    }
}