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
    suspend fun insertTask(task: ToDo)

    @Query("SELECT * FROM ToDo")
    fun loadAllTasks(): Flow<List<ToDo>> // change

    @Update
    suspend fun updateTask(task: ToDo) // change

    @Delete
    suspend fun deleteTask(task: ToDo) // change
}