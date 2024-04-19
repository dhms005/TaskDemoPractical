package com.taskdemo.practical.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.taskdemo.practical.model.TaskModel


@Dao
interface TaskDao {

    @Insert()
    suspend fun insertTask(taskModel: TaskModel): Long

    @Query("Select * from TaskModel where isFinished == 0")
    fun getTask(): LiveData<List<TaskModel>>

    @Query("Select * from TaskModel where isFinished == 1")
    fun getFinishedTask(): LiveData<List<TaskModel>>

    @Query("Update TaskModel Set isFinished = 1 where id=:uid")
    fun finishTask(uid: Long)

    @Query("Update TaskModel Set isFinished = 0 where id=:uid")
    fun unFinishTask(uid: Long)

    @Query("Delete from TaskModel where id=:uid")
    fun deleteTask(uid: Long)

    @Query("Update TaskModel Set isNotification = :isNotification where id=:uid")
    fun notificationOnOffTask(uid: Long, isNotification: Int)

    @Query("SELECT * FROM TaskModel WHERE id=:id ")
    fun getDataById(id: Long): LiveData<TaskModel>

    @Query("SELECT * FROM TaskModel WHERE id=:id ")
    fun getTaskById(id: Long): TaskModel

    @Query("DELETE FROM TaskModel")
    fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(taskModel: TaskModel)
}