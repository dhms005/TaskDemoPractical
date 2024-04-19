package com.taskdemo.practical.utils.broadcastReceivers

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.os.*
import android.util.Log
import androidx.preference.PreferenceManager
import com.taskdemo.practical.BaseApp
import com.taskdemo.practical.R
import com.taskdemo.practical.activity.TaskDetailsActivity
import com.taskdemo.practical.db.AppDatabase
import com.taskdemo.practical.utils.NotificationHelper
import com.taskdemo.practical.utils.services.TodoDoneIntentService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * Created by Dhms on 19/4/24.
 */
class AlarmReceiver : BroadcastReceiver() {

    var defaultSharedPreferences: SharedPreferences? = null
    private var checkNotification = true
    var prefVibration = true


    override fun onReceive(context: Context, intent: Intent) {

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        readData(context)
//        val db = AppDatabase.getDatabase(context)
        val db by lazy {
            AppDatabase.getDatabase(context)
        }

        val notificationHelper = NotificationHelper(context)
        val id = intent.getLongExtra("id", 0)
        val body = intent.getStringExtra("body")
        val title = intent.getStringExtra("title")
        val notificationIntent = Intent(context, TaskDetailsActivity::class.java)

        notificationIntent.putExtra("id", id)


        val notificationPendingIntent = PendingIntent.getActivity(
            context,
            id.toInt(),
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationActionIntent = Intent(context, TodoDoneIntentService::class.java)
        notificationActionIntent.putExtra("id", id)

        val notificationActionPendingIntent = PendingIntent.getService(
            context,
            id.toInt(),
            notificationActionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val action = Notification.Action.Builder(
            Icon.createWithResource(context, R.mipmap.ic_done_white_24dp),
            "Done!",
            notificationActionPendingIntent
        ).build()


        GlobalScope.launch(Dispatchers.IO) {


            try {
                if (db.todoDao().getTaskById(id).isNotification == 1 && checkNotification) {
                    notificationHelper.notify(
                        id.toInt(),
                        title,
                        body,
                        notificationPendingIntent,
                        action
                    )

                    if (prefVibration) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(
                                    VibrationEffect.createOneShot(
                                        500,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                            } else {
                                v.vibrate(500)
                            }
                        }, 1000)
                    }
                }
            } catch (ce: CancellationException) {
                // You can ignore or log this exception
            } catch (e: Exception) {
                // Here it's better to at least log the exception
                Log.e("TAG", "Coroutine error", e)

            }

        }


    }

    private fun readData(context: Context) {
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        checkNotification = defaultSharedPreferences.getBoolean("prefNotification", true)
        prefVibration = defaultSharedPreferences.getBoolean("prefVibration", true)
    }
}