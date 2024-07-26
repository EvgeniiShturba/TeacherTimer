package com.shturba.teachertimer.ui.lesson

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shturba.teachertimer.database.Lesson
import com.shturba.teachertimer.database.LessonDatabase
import com.shturba.teachertimer.database.Repository
import com.shturba.teachertimer.model.LessonActivity
import com.shturba.teachertimer.utils.TEACHER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

private const val MINUTES_45 = 45 * 60 * 1_000L
private const val SECONDS = 8 * 1_000L

data class UiState(
    val timerValue: String = "",
    val currentActivity: LessonActivity = LessonActivity.ADMINISTRATIVE,
    val isLessonFinished: Boolean = false,
)

class LessonViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = Repository.getInstance(
        LessonDatabase.getDatabase(app).lessonDao(),
        TEACHER_ID,
    )
    private var startTime: Long = -1
    private var currentActivity: LessonActivity? = null
    private val totalTime = mutableMapOf<LessonActivity, Long>()

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> get() = _uiState

    private val timer = object : CountDownTimer(MINUTES_45, 1_000) {
        override fun onTick(millisUntilFinished: Long) {
            val minutesString = (millisUntilFinished / 60_000).toString().padStart(2, '0')
            val secondsString = (millisUntilFinished / 1000 % 60).toString().padStart(2, '0')
            _uiState.value = _uiState.value?.copy(timerValue = "$minutesString:$secondsString")
        }

        override fun onFinish() {
            stop()
        }
    }

    init {
        currentActivity = LessonActivity.ADMINISTRATIVE
        startTime = System.currentTimeMillis()
        timer.start()
    }

    fun changeActivity(newActivity: LessonActivity) {
        val currentActivityNonNull = requireNotNull(currentActivity)
        if (currentActivityNonNull == newActivity) return

        val now = System.currentTimeMillis()
        val duration = now - startTime

        val accountedTime = totalTime[currentActivityNonNull] ?: 0
        totalTime[currentActivityNonNull] = accountedTime + duration

        startTime = now
        currentActivity = newActivity
        _uiState.value = _uiState.value?.copy(currentActivity = newActivity)
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
            _uiState.postValue(_uiState.value?.copy(isLessonFinished = true))
        }
    }
}
