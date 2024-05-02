package com.shturba.teachertimer.database

import kotlinx.coroutines.flow.Flow

class Repository private constructor(private val lessonDao: LessonDao, teacherId: String) {

    val lessons: Flow<List<Lesson>> = lessonDao.getLessons(teacherId)

    suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insert(lesson)
    }
    companion object {

        @Volatile
        private var instance: Repository? = null

        fun getInstance(lessonDao: LessonDao, teacherId: String) =
            instance ?: synchronized(this) {
                instance ?: Repository(lessonDao, teacherId).also { instance = it }
            }
    }
}