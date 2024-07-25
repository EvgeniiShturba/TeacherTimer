package com.shturba.teachertimer.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shturba.teachertimer.database.LessonDatabase
import com.shturba.teachertimer.database.Repository
import com.shturba.teachertimer.database.toLessonData
import com.shturba.teachertimer.ui.lesson.LessonActivity
import com.shturba.teachertimer.utils.TEACHER_ID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ReportViewModel(
    app: Application,
) : AndroidViewModel(app) {

    private val repo =
        Repository.getInstance(LessonDatabase.getDatabase(app).lessonDao(), TEACHER_ID)
    private val lessons = repo.lessons.asLiveData()

    private var countdownJob: Job? = null
    private val _countdown = MutableLiveData(5)
    val countdown: LiveData<Int> = _countdown
    private val _isLessonStarted = MutableLiveData(false)
    val isLessonStarted: LiveData<Boolean> = _isLessonStarted

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

    fun startCountdown() {
        countdownJob = viewModelScope.launch {
            for (n in 5 downTo 1) {
                if (isActive) {
                    _countdown.postValue(n)
                    delay(1_000)
                }
            }
            if (isActive) {
                _isLessonStarted.postValue(true)
            }
        }
    }

    fun cancelCountdown() {
        countdownJob?.cancel()
    }

    fun isLessonStartedHandled() {
        _isLessonStarted.value = false
    }
}
