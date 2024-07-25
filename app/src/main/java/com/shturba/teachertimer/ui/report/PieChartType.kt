package com.shturba.teachertimer.ui.report

enum class PieChartType(
    val position: Int,
    val title: String,
) {
    LAST_LESSON(position = 1, title = "Last lesson"),
    ALL_LESSONS(position = 2, title = "All lessons"),
    ;

    companion object {
        fun from(tabPosition: Int) = entries.firstOrNull { it.position == tabPosition }
    }
}