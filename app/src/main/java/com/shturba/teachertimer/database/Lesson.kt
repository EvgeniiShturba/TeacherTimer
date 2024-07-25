package com.shturba.teachertimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shturba.teachertimer.ui.lesson.LessonActivity

@Entity
data class Lesson (
    @PrimaryKey  val uuid: String,
    @ColumnInfo(name ="teacher_id")  val teacherId: String,
    @ColumnInfo val timestamp: Long,
    @ColumnInfo val administrative: Int,
    @ColumnInfo (name ="new_material") val newMaterial: Int,
    @ColumnInfo  val checking: Int,
    @ColumnInfo val testing: Int,
    @ColumnInfo  val communication: Int,
)

fun Lesson.toLessonData(): Map<LessonActivity, Int> = mapOf(
    LessonActivity.ADMINISTRATIVE to administrative,
    LessonActivity.NEW_MATERIAL to newMaterial,
    LessonActivity.CHECKING to checking,
    LessonActivity.TESTING to testing,
    LessonActivity.COMMUNICATION to communication,
)

fun List<Lesson>.toLessonData(): Map<LessonActivity, Int> {
    val administrative = this.fold(0) { acc, lesson ->
        acc + lesson.administrative
    }
    val newMaterial = this.fold(0) { acc, lesson ->
        acc + lesson.newMaterial
    }
    val checking = this.fold(0) { acc, lesson ->
        acc + lesson.checking
    }
    val testing = this.fold(0) { acc, lesson ->
        acc + lesson.testing
    }
    val communication = this.fold(0) { acc, lesson ->
        acc + lesson.communication
    }

    return mutableMapOf(
        LessonActivity.ADMINISTRATIVE to administrative,
        LessonActivity.NEW_MATERIAL to newMaterial,
        LessonActivity.CHECKING to checking,
        LessonActivity.TESTING to testing,
        LessonActivity.COMMUNICATION to communication,
    )
}