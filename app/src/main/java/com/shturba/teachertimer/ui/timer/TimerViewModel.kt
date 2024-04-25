package com.shturba.teachertimer.ui.timer

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shturba.teachertimer.database.Lesson
import com.shturba.teachertimer.database.LessonDao
import com.shturba.teachertimer.database.LessonDatabase
import com.shturba.teachertimer.database.Repository
import com.shturba.teachertimer.utils.TEACHER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class TimerViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = Repository(LessonDatabase.getDatabase(app).lessonDao(), TEACHER_ID)
    private var startTime: Long = -1
    private var currentActivity: LessonActivity? = null
    private val totalTime = mutableMapOf<LessonActivity, Long>()

    private val _lessonEnd = MutableLiveData(false)
    val lessonEnd: LiveData<Boolean> get() = _lessonEnd

    private val _timerValue = MutableLiveData("")
    val timerValue: LiveData<String> get() = _timerValue

    //    private val timer = object : CountDownTimer(45 * 60 * 1_000, 1_000) {
    private val timer = object : CountDownTimer(15 * 1_000, 1_000) {
        override fun onTick(millisUntilFinished: Long) {
            val minutesString = (millisUntilFinished / 60_000).toString().padStart(2, '0')
            val secondsString = (millisUntilFinished / 1000 % 60).toString().padStart(2, '0')
            _timerValue.value = "$minutesString:$secondsString"
        }

        override fun onFinish() {
            stop()
        }
    }

    fun start() {
        currentActivity = LessonActivity.ADMINISTRATIVE
        startTime = System.currentTimeMillis()
        timer.start()
    }

    fun changeActivity(newActivity: LessonActivity) {
        val currentActivityNonNull = currentActivity ?: return
        if (currentActivity == newActivity) return

        val now = System.currentTimeMillis()
        val duration = now - startTime

        val accountedTime = totalTime[currentActivityNonNull] ?: 0
        totalTime[currentActivityNonNull] = accountedTime + duration

        startTime = now
        currentActivity = newActivity
    }

    fun stop() {
        val currentActivityNonNull = currentActivity ?: return

        val now = System.currentTimeMillis()
        val duration = now - startTime

        val accountedTime = totalTime[currentActivityNonNull] ?: 0
        totalTime[currentActivityNonNull] = accountedTime + duration

        viewModelScope.launch(Dispatchers.IO) {
            val lesson = Lesson(
                uuid = UUID.randomUUID().toString(),
                teacherId = TEACHER_ID,
                timestamp = System.currentTimeMillis(),
                administrative = ((totalTime[LessonActivity.ADMINISTRATIVE] ?: 0) / 1_000).toInt(),
                newMaterial = ((totalTime[LessonActivity.NEW_MATERIAL] ?: 0) / 1_000).toInt(),
                checking = ((totalTime[LessonActivity.CHECKING] ?: 0) / 1_000).toInt(),
                testing = ((totalTime[LessonActivity.TESTING] ?: 0) / 1_000).toInt(),
                communication = ((totalTime[LessonActivity.COMMUNICATION] ?: 0) / 1_000).toInt(),
                )
            repo.insertLesson(lesson)
            _lessonEnd.postValue(true)
        }
    }
}

enum class LessonActivity {
    ADMINISTRATIVE,
    NEW_MATERIAL,
    CHECKING,
    TESTING,
    COMMUNICATION;
}
