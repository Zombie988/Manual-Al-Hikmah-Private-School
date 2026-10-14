package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "learning_progress",
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
data class LearningProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val subjectCode: String, // ARABIC, MATH, SCIENCE, ENGLISH, FRENCH, HISTORY_GEO, INFORMATICS, CIVICS
    val subjectTitleArabic: String,
    val subjectTitleEnglish: String,
    val assessmentType: String, // QUIZ_1, QUIZ_2, ORAL, ACTIVITY, MIDTERM, FINAL
    val assessmentTitleArabic: String,
    val score: Float,
    val maxScore: Float,
    val topicArabic: String,
    val topicEnglish: String,
    val teacherNotes: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val percentage: Float
        get() = if (maxScore > 0f) (score / maxScore) * 100f else 0f
}
