package com.taskdemo.practical.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

// we are making list for each task
@Entity
data class TaskModel(
    var title: String,
    var description: String,
    var date: Long,
    var time: Long,
    var timestamp: Long,
    var isFinished: Int = 0,
    var isNotification: Int = 1,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
