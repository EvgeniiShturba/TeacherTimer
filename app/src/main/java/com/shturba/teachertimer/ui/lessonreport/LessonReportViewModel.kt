package com.shturba.teachertimer.ui.lessonreport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.shturba.teachertimer.database.LessonDatabase
import com.shturba.teachertimer.database.Repository
import com.shturba.teachertimer.database.toLessonData
import com.shturba.teachertimer.ui.timer.LessonActivity
import com.shturba.teachertimer.utils.TEACHER_ID

class LessonReportViewModel(app: Application) : AndroidViewModel(app) {

    private val repo =
        Repository.getInstance(LessonDatabase.getDatabase(app).lessonDao(), TEACHER_ID)

    private val lessons = repo.lessons.asLiveData()

    val lastLessonData = MediatorLiveData<Map<LessonActivity, Int>?>().apply {
        addSource(lessons) { lessonList ->
            value = lessonList.maxByOrNull { it.timestamp }?.toLessonData()
        }
    }
    val allLessonsData = MediatorLiveData<Map<LessonActivity, Int>>().apply {
        addSource(lessons) { lessonList ->
            value = lessonList.toLessonData()
        }
    }
}