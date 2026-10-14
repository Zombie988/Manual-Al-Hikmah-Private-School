package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDemerit
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.SchoolTeal
import com.example.ui.viewmodel.AppLanguage

@Composable
fun StudentSelectorStrip(
    students: List<StudentEntity>,
    selectedStudentId: Long?,
    selectedGrade: String,
    language: AppLanguage,
    onSelectGrade: (String) -> Unit,
    onSelectStudent: (Long) -> Unit,
    onAddNewStudentClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.ARABIC

    val firstGradeCount = students.count { it.gradeLevel.contains("الأول") || it.gradeLevel.contains("First") }
    val thirdLevelCount = students.count { it.gradeLevel.contains("الثالثة") || it.gradeLevel.contains("Third") || it.gradeLevel.contains("KG3") }
    val totalCount = students.size

    val gradeTabs = listOf(
        Triple(
            "FIRST_GRADE",
            if (isAr) "الصف الأول ($firstGradeCount)" else "First Grade ($firstGradeCount)",
            if (isAr) "الأساسي" else "Primary"
        ),
        Triple(
            "THIRD_LEVEL",
            if (isAr) "الفئة الثالثة ($thirdLevelCount)" else "Level 3 ($thirdLevelCount)",
            if (isAr) "رياض أطفال" else "Kindergarten"
        ),
        Triple(
            "ALL",
            if (isAr) "جميع الصفوف ($totalCount)" else "All Grades ($totalCount)",
            if (isAr) "الكل" else "All"
        )
    )

    // Filter students by selectedGrade for display in the strip
    val displayedStudents = when (selectedGrade) {
        "FIRST_GRADE" -> students.filter { it.gradeLevel.contains("الأول") || it.gradeLevel.contains("First") }
        "THIRD_LEVEL" -> students.filter { it.gradeLevel.contains("الثالثة") || it.gradeLevel.contains("Third") || it.gradeLevel.contains("KG3") }
        else -> students
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFEEF2F6))
            .padding(vertical = 8.dp)
    ) {
        // Grade Switcher Tabs: Make each grade in a separate list for ease of use
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                gradeTabs.forEach { (key, title, subtitle) ->
                    val isSelected = selectedGrade == key
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SchoolNavy else Color.White,
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) SchoolNavy else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectGrade(key) }
                            .testTag("grade_tab_$key")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (key == "FIRST_GRADE") Icons.Default.School else Icons.Default.Class,
                                contentDescription = null,
                                tint = if (isSelected) SchoolGold else SchoolNavy,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF334155),
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Student Chips Row (filtered by selected grade)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            displayedStudents.forEach { student ->
                val isSelected = student.id == selectedStudentId
                val parsedColor = try {
                    Color(android.graphics.Color.parseColor(student.avatarColorHex))
                } catch (e: Exception) {
                    SchoolNavy
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
                    shadowElevation = if (isSelected) 3.dp else 0.dp,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) SchoolNavy else Color(0xFFCBD5E1),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onSelectStudent(student.id) }
                        .testTag("student_chip_${student.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar Circle
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(parsedColor),
                            contentAlignment = Alignment.Center
                        ) {
                            val initial = if (isAr) {
                                student.fullNameArabic.firstOrNull()?.toString() ?: "ط"
                            } else {
                                student.fullNameEnglish.firstOrNull()?.toString() ?: "S"
                            }
                            Text(
                                text = initial,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = if (isAr) student.fullNameArabic else student.fullNameEnglish,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SchoolNavy else Color(0xFF1E293B),
                                    fontSize = 12.5.sp
                                )
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val attColor = when (student.attendanceStatus) {
                                    "PRESENT" -> EmeraldSuccess
                                    "LATE" -> SchoolGold
                                    else -> RoseDemerit
                                }
                                val attText = when (student.attendanceStatus) {
                                    "PRESENT" -> if (isAr) "حاضر" else "Present"
                                    "LATE" -> if (isAr) "متأخر" else "Late"
                                    "EXCUSED" -> if (isAr) "مبرر" else "Excused"
                                    else -> if (isAr) "غائب" else "Absent"
                                }
                                Text(
                                    text = attText,
                                    color = attColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                )

                                // Grade badge if viewing ALL
                                if (selectedGrade == "ALL") {
                                    val isG1 = student.gradeLevel.contains("الأول") || student.gradeLevel.contains("First")
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (isG1) SchoolTeal.copy(alpha = 0.15f) else SchoolNavy.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = if (isG1) (if (isAr) "الصف 1" else "G1") else (if (isAr) "فئة 3" else "KG3"),
                                            color = if (isG1) SchoolTeal else SchoolNavy,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Optional Add Student Chip for teachers
            if (onAddNewStudentClick != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .border(1.dp, SchoolNavy.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onAddNewStudentClick() }
                        .testTag("add_student_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = SchoolNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAr) "إضافة طالب" else "Add Student",
                            color = SchoolNavy,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
