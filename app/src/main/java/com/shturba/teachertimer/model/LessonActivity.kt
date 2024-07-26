package com.shturba.teachertimer.model

import android.graphics.Color

enum class LessonActivity(val color: Int) {
    ADMINISTRATIVE(color = Color.CYAN),
    NEW_MATERIAL(color = Color.GREEN),
    CHECKING(color = Color.MAGENTA),
    TESTING(color = Color.RED),
    COMMUNICATION(color = Color.YELLOW),
}
