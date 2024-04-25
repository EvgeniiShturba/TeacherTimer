package com.shturba.teachertimer.database

import kotlinx.coroutines.flow.Flow

class Repository(private val lessonDao: LessonDao, teacherId: String) {

    val lessons: Flow<List<Lesson>> = lessonDao.getLessons(teacherId)

    suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insert(lesson)
    }
}