package com.shturba.teachertimer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Insert
    suspend fun insert(lesson: Lesson)

    @Query("SELECT * FROM lesson WHERE teacher_id = :teacherId")
    fun getLessons(teacherId: String ): Flow<List<Lesson>>

}
