package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "side_notes",
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
data class SideNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val teacherName: String,
    val subject: String, // e.g. "اللغة العربية", "الرياضيات", "الإرشاد النفسي والتربوي", "الإدارة المدرسية"
    val category: String, // ACADEMIC, BEHAVIORAL, ADMINISTRATIVE, HEALTH, PRAISE
    val noteText: String,
    val isUrgent: Boolean = false,
    val isAcknowledgedByParent: Boolean = false,
    val parentResponse: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val acknowledgedAt: Long? = null
)
