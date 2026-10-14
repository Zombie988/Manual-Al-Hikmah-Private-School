package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents the exact evaluation structure used by Manhal Al-Hikmah Private School
 * (مدرسة منهل الحكمة الخاصة):
 * - الناحية التعليمية: القراءة، الكتابة، الخط، سرعة الكتابة، النطق (جيد / متوسط / ضعيف / -)
 * - الناحية السلوكية: انعزالي، عدواني، متردد، مؤدب، ملول، بطيء (نعم / لا)
 * - خانة الملاحظات: مرض معين، دخول حمام متكرر، حالة انفصال، وتوجيهات خاصة
 */
@Entity(
    tableName = "student_evaluations",
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
data class StudentEvaluationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val subject: String = "اللغة العربية",
    val gradeLevel: String = "الفئة الثالثة",
    val teacherName: String = "أ. غادة الرفاعي",

    // الناحية التعليمية: (جيد، متوسط، ضعيف، -)
    val reading: String = "جيد",
    val writing: String = "متوسط",
    val handwriting: String = "متوسط",
    val writingSpeed: String = "متوسط",
    val pronunciation: String = "جيد",

    // الناحية السلوكية والاجتماعية: (نعم = true، لا = false)
    val isWithdrawn: Boolean = false,   // انعزالي
    val isAggressive: Boolean = false,  // عدواني
    val isHesitant: Boolean = false,    // متردد
    val isPolite: Boolean = true,       // مؤدب
    val isEasilyBored: Boolean = false, // ملول
    val isSlow: Boolean = false,        // بطيء

    // خانة الملاحظات (مرض معين أو دخول حمام متكرر أو حالة انفصال)
    val specialNotes: String = "",
    val hasFrequentRestroom: Boolean = false,
    val hasFamilySeparation: Boolean = false,
    val hasHealthCondition: Boolean = false,

    val isAcknowledgedByParent: Boolean = false,
    val parentReply: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
