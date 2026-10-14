package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "behavior_records",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId")]
)
data class BehaviorRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val pointsDelta: Int, // e.g. +5, +10, -5, -3
    val category: String, // PARTICIPATION, DISCIPLINE, HOMEWORK, LEADERSHIP, TARDINESS, DISTRACTION, UNIFORM, OTHER
    val titleArabic: String,
    val titleEnglish: String,
    val note: String,
    val teacherName: String,
    val timestamp: Long = System.currentTimeMillis()
)
