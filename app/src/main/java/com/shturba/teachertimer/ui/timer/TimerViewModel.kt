package com.shturba.teachertimer.ui.timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private var startTime: Long = -1
    private var currentActivity: LessonActivity? = null
    private val totalTime = mutableMapOf<LessonActivity, Long>()

    private val _timerValue = MutableLiveData("")
    val timerValue: LiveData<String> get() = _timerValue

    private val timer = object : CountDownTimer(45 * 60 * 1_000, 1_000) {
        override fun onTick(millisUntilFinished: Long) {
            _timerValue.value = "${millisUntilFinished / 60_000}:${millisUntilFinished / 1000 % 60}"
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

        currentActivity = null
        startTime = -1
    }
}

enum class LessonActivity {
    ADMINISTRATIVE,
    NEW_MATERIAL,
    CHECKING,
    TESTING,
    COMMUNICATION;
}
