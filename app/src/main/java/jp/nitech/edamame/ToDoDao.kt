package jp.nitech.edamame

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ToDoDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task")
    fun loadAllTasks(): Flow<List<Task>> // change

    @Update
    suspend fun updateTask(task: Task) // change

    @Delete
    suspend fun deleteTask(task: Task) // change
}