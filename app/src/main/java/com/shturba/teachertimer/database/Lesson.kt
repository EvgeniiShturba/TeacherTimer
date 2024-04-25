package com.shturba.teachertimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
