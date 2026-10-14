package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentCode: String,
    val fullNameArabic: String,
    val fullNameEnglish: String,
    val gradeLevel: String, // e.g. "الصف السابع - الشعبة أ" / "Grade 7 - Section A"
    val guardianName: String,
    val guardianPhone: String,
    val attendanceStatus: String = "PRESENT", // PRESENT, LATE, ABSENT, EXCUSED
    val avatarColorHex: String = "#1E3A8A",
    val birthDate: String = "2012-04-15"
)
