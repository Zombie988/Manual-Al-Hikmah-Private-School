package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity
import com.example.ui.components.AddBehaviorDialog
import com.example.ui.components.AddProgressDialog
import com.example.ui.components.AddSideNoteDialog
import com.example.ui.components.AddStudentDialog
import com.example.ui.components.EditEvaluationDialog
import com.example.ui.components.OfficialEvaluationCard
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDemerit
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.SchoolTeal
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.StudentPerformanceSummary

@Composable
fun TeacherPortalScreen(
    students: List<StudentEntity>,
    allEvaluations: List<StudentEvaluationEntity>,
    selectedStudent: StudentEntity?,
    summary: StudentPerformanceSummary,
    language: AppLanguage,
    selectedGrade: String = "FIRST_GRADE",
    onSelectGrade: (String) -> Unit = {},
    onSelectStudent: (Long) -> Unit,
    onUpdateAttendance: (studentId: Long, status: String) -> Unit,
    onSaveEvaluation: (StudentEvaluationEntity) -> Unit,
    onRecordBehavior: (studentId: Long, points: Int, category: String, titleAr: String, titleEn: String, note: String, teacher: String) -> Unit,
    onRecordProgress: (studentId: Long, subjectCode: String, subjectAr: String, subjectEn: String, assessmentType: String, assessmentTitleAr: String, score: Float, maxScore: Float, topicAr: String, topicEn: String, notes: String) -> Unit,
    onAddSideNote: (studentId: Long, teacher: String, subject: String, category: String, text: String, isUrgent: Boolean) -> Unit,
    onAddNewStudent: (fullNameAr: String, fullNameEn: String, grade: String, guardian: String, phone: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.ARABIC

    var showBehaviorDialog by remember { mutableStateOf(false) }
    var showProgressDialog by remember { mutableStateOf(false) }
    var showSideNoteDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showEditEvaluationDialog by remember { mutableStateOf(false) }
    var showFullGridSheet by remember { mutableStateOf(false) }

    // Separate Grade Lists
    val firstGradeStudents = remember(students) {
        students.filter { it.gradeLevel.contains("الأول") || it.gradeLevel.contains("First") }
    }
    val thirdLevelStudents = remember(students) {
        students.filter { it.gradeLevel.contains("الثالثة") || it.gradeLevel.contains("Third") || it.gradeLevel.contains("KG3") }
    }

    if (showBehaviorDialog && selectedStudent != null) {
        AddBehaviorDialog(
            language = language,
            onDismiss = { showBehaviorDialog = false },
            onSubmit = { points, category, titleAr, titleEn, note, teacher ->
                onRecordBehavior(selectedStudent.id, points, category, titleAr, titleEn, note, teacher)
            }
        )
    }

    if (showProgressDialog && selectedStudent != null) {
        AddProgressDialog(
            language = language,
            onDismiss = { showProgressDialog = false },
            onSubmit = { subjectCode, subjectAr, subjectEn, assessmentType, assessmentTitleAr, score, maxScore, topicAr, topicEn, notes ->
                onRecordProgress(
                    selectedStudent.id,
                    subjectCode,
                    subjectAr,
                    subjectEn,
                    assessmentType,
                    assessmentTitleAr,
                    score,
                    maxScore,
                    topicAr,
                    topicEn,
                    notes
                )
            }
        )
    }

    if (showSideNoteDialog && selectedStudent != null) {
        AddSideNoteDialog(
            language = language,
            onDismiss = { showSideNoteDialog = false },
            onSubmit = { teacher, subject, category, text, isUrgent ->
                onAddSideNote(selectedStudent.id, teacher, subject, category, text, isUrgent)
            }
        )
    }

    if (showAddStudentDialog) {
        AddStudentDialog(
            language = language,
            onDismiss = { showAddStudentDialog = false },
            onSubmit = { fullNameAr, fullNameEn, grade, guardian, phone ->
                onAddNewStudent(fullNameAr, fullNameEn, grade, guardian, phone)
            }
        )
    }

    if (showEditEvaluationDialog && selectedStudent != null) {
        EditEvaluationDialog(
            student = selectedStudent,
            currentEval = summary.evaluation,
            language = language,
            onDismiss = { showEditEvaluationDialog = false },
            onSave = { updated ->
                onSaveEvaluation(updated)
                showEditEvaluationDialog = false
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Teacher Banner - Adapts to Selected Grade
        item {
            val bannerTeacher = when (selectedGrade) {
                "FIRST_GRADE" -> if (isAr) "أ. ريم الحلبي" else "Ms. Reem Al-Halabi"
                "THIRD_LEVEL" -> if (isAr) "أ. غادة الرفاعي" else "Ms. Ghada Al-Rifai"
                else -> if (isAr) "الكادر التعليمي - جميع الصفوف" else "Teaching Staff"
            }
            val bannerGrade = when (selectedGrade) {
                "FIRST_GRADE" -> if (isAr) "الصف الأول الأساسي (الشعبة الأولى)" else "First Grade (Section 1)"
                "THIRD_LEVEL" -> if (isAr) "الفئة الثالثة (شعبة النور) - رياض الأطفال" else "Level 3 (Al-Noor) - Kindergarten"
                else -> if (isAr) "الصف الأول والفئة الثالثة" else "All Enrolled Classes"
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = SchoolNavy),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "مدرسة منهل الحكمة الخاصة - بوابة المعلمة" else "Manhal Al-Hikmah - Staff Portal",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isAr) "المادة: اللغة العربية • الصف: $bannerGrade • المعلمة: $bannerTeacher"
                                else "Subject: Arabic • Class: $bannerGrade • Teacher: $bannerTeacher",
                                color = SchoolGold,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { showAddStudentDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolGold),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("teacher_add_student_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp), tint = SchoolNavy)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isAr) "طالب جديد" else "New", fontSize = 12.sp, color = SchoolNavy, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Instruction banner verbatim from school sheet
                    Surface(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isAr)
                                "معلمتنا الكريمة: التقييم في (الناحية التعليمية) كلمة (جيد أو متوسط أو ضعيف)، وفي (الناحية السلوكية والاجتماعية) كلمة (نعم أو لا)، وفي خانة الملاحظات يكتب إن كان يعاني من مرض أو دخول حمام متكرر أو حالة انفصال."
                            else
                                "Official Instructions: Learning skills evaluated (Good, Average, Weak); Behavioral traits (Yes/No); Notes column records illness, frequent restroom, or separation cases.",
                            color = Color.White,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // Grade Selector Tabs Bar: Make each grade in a separate list for ease of use
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (isAr) "تصفح الصفوف الدراسية (قوائم منفصلة لسهولة الاستخدام):" else "Browse Grades (Separate Lists for Ease of Use):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val gTabs = listOf(
                            Triple("FIRST_GRADE", if (isAr) "الصف الأول (${firstGradeStudents.size})" else "Grade 1 (${firstGradeStudents.size})", Icons.Default.School),
                            Triple("THIRD_LEVEL", if (isAr) "الفئة الثالثة (${thirdLevelStudents.size})" else "Level 3 (${thirdLevelStudents.size})", Icons.Default.Class),
                            Triple("ALL", if (isAr) "جميع الصفوف (${students.size})" else "All (${students.size})", Icons.Default.Assignment)
                        )
                        gTabs.forEach { (key, label, icon) ->
                            val isSel = selectedGrade == key
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) SchoolNavy else Color(0xFFF1F5F9),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSelectGrade(key) }
                                    .testTag("teacher_grade_tab_$key")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSel) SchoolGold else Color(0xFF475569),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else Color(0xFF334155),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Toggle: Full Class Grid Sheet vs Student Card View
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAr) "طريقة العرض:" else "View Mode:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (showFullGridSheet) SchoolNavy else Color.White,
                    modifier = Modifier
                        .border(1.dp, SchoolNavy, RoundedCornerShape(20.dp))
                        .clickable { showFullGridSheet = !showFullGridSheet }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TableChart,
                            contentDescription = null,
                            tint = if (showFullGridSheet) Color.White else SchoolNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (showFullGridSheet) (if (isAr) "العودة لبطاقات الطلاب" else "Cards View")
                            else (if (isAr) "عرض جدول الاستمارة الكامل (كما بالورقة)" else "View Full Paper Grid Table"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (showFullGridSheet) Color.White else SchoolNavy
                        )
                    }
                }
            }
        }

        // IF Full Grid Sheet view is enabled:
        if (showFullGridSheet) {
            val gridStudents = when (selectedGrade) {
                "FIRST_GRADE" -> firstGradeStudents
                "THIRD_LEVEL" -> thirdLevelStudents
                else -> students
            }
            val gridTitle = when (selectedGrade) {
                "FIRST_GRADE" -> if (isAr) "استمارة متابعة الصف الأول الأساسي (معلمة المادة: ريم الحلبي)" else "First Grade Comprehensive Sheet (Teacher: Reem Al-Halabi)"
                "THIRD_LEVEL" -> if (isAr) "استمارة متابعة الفئة الثالثة - شعبة النور (معلمة المادة: غادة الرفاعي)" else "Level 3 Comprehensive Sheet (Teacher: Ghada Al-Rifai)"
                else -> if (isAr) "استمارة المتابعة الشاملة لجميع الصفوف" else "All Classes Comprehensive Sheet"
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = gridTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SchoolNavy
                        )
                        Text(
                            text = if (isAr) "انقر على أي سطر لتعديل التقييم مباشرة • المجموع: ${gridStudents.size} طالباً"
                            else "Tap any row to edit ratings directly • Total: ${gridStudents.size} students",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Horizontally scrollable paper table replica
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            Column {
                                // Table Header
                                Row(
                                    modifier = Modifier
                                        .background(Color(0xFFEEF2F6))
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    GridCell(if (isAr) "اسم التلميذ" else "Name", width = 110.dp, isHeader = true)
                                    GridCell(if (isAr) "القراءة" else "Read", width = 60.dp, isHeader = true)
                                    GridCell(if (isAr) "الكتابة" else "Write", width = 60.dp, isHeader = true)
                                    GridCell(if (isAr) "الخط" else "Handw", width = 60.dp, isHeader = true)
                                    GridCell(if (isAr) "السرعة" else "Speed", width = 60.dp, isHeader = true)
                                    GridCell(if (isAr) "النطق" else "Speech", width = 60.dp, isHeader = true)
                                    GridCell(if (isAr) "انعزالي" else "Withd", width = 55.dp, isHeader = true)
                                    GridCell(if (isAr) "عدواني" else "Aggr", width = 55.dp, isHeader = true)
                                    GridCell(if (isAr) "متردد" else "Hesit", width = 55.dp, isHeader = true)
                                    GridCell(if (isAr) "مؤدب" else "Polite", width = 55.dp, isHeader = true)
                                    GridCell(if (isAr) "ملول" else "Bored", width = 55.dp, isHeader = true)
                                    GridCell(if (isAr) "بطيء" else "Slow", width = 55.dp, isHeader = true)
                                    GridCell(if (isAr) "الملاحظات الخاصة" else "Notes", width = 180.dp, isHeader = true)
                                }

                                HorizontalDivider(color = Color(0xFFCBD5E1))

                                // Table Rows for each student
                                gridStudents.forEachIndexed { idx, s ->
                                    val ev = allEvaluations.find { it.studentId == s.id }
                                        ?: StudentEvaluationEntity(studentId = s.id)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(if (idx % 2 == 0) Color.White else Color(0xFFF8FAFC))
                                            .clickable {
                                                onSelectStudent(s.id)
                                                showEditEvaluationDialog = true
                                            }
                                            .padding(vertical = 6.dp, horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        GridCell(if (isAr) s.fullNameArabic else s.fullNameEnglish, width = 110.dp, isBold = true)
                                        GridCell(ev.reading, width = 60.dp, color = ratingColor(ev.reading))
                                        GridCell(ev.writing, width = 60.dp, color = ratingColor(ev.writing))
                                        GridCell(ev.handwriting, width = 60.dp, color = ratingColor(ev.handwriting))
                                        GridCell(ev.writingSpeed, width = 60.dp, color = ratingColor(ev.writingSpeed))
                                        GridCell(ev.pronunciation, width = 60.dp, color = ratingColor(ev.pronunciation))
                                        GridCell(if (ev.isWithdrawn) "نعم" else "لا", width = 55.dp, color = if (ev.isWithdrawn) RoseDemerit else Color(0xFF64748B))
                                        GridCell(if (ev.isAggressive) "نعم" else "لا", width = 55.dp, color = if (ev.isAggressive) RoseDemerit else Color(0xFF64748B))
                                        GridCell(if (ev.isHesitant) "نعم" else "لا", width = 55.dp, color = if (ev.isHesitant) RoseDemerit else Color(0xFF64748B))
                                        GridCell(if (ev.isPolite) "نعم" else "لا", width = 55.dp, color = if (ev.isPolite) EmeraldSuccess else Color(0xFF64748B))
                                        GridCell(if (ev.isEasilyBored) "نعم" else "لا", width = 55.dp, color = if (ev.isEasilyBored) RoseDemerit else Color(0xFF64748B))
                                        GridCell(if (ev.isSlow) "نعم" else "لا", width = 55.dp, color = if (ev.isSlow) RoseDemerit else Color(0xFF64748B))
                                        GridCell(
                                            text = buildString {
                                                if (ev.hasFrequentRestroom) append("حمام متكرر | ")
                                                if (ev.hasFamilySeparation) append("انفصال | ")
                                                if (ev.specialNotes.isNotBlank()) append(ev.specialNotes)
                                            }.ifBlank { "-" },
                                            width = 180.dp,
                                            fontSize = 10.sp
                                        )
                                    }
                                    HorizontalDivider(color = Color(0xFFF1F5F9))
                                }
                            }
                        }
                    }
                }
            }
        }

        if (selectedStudent != null && !showFullGridSheet) {
            // Selected Student Official Evaluation Card
            item {
                OfficialEvaluationCard(
                    student = selectedStudent,
                    evaluation = summary.evaluation,
                    language = language,
                    onAcknowledgeClick = { },
                    onEditClick = { showEditEvaluationDialog = true }
                )
            }

            // Quick Control Card for Selected Student
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = if (isAr) "تسجيل سريع وإجراءات المعلمة:" else "Quick Staff Actions:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "${selectedStudent.gradeLevel} • ${selectedStudent.studentCode}",
                                    fontSize = 11.sp,
                                    color = SchoolTeal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Quick Attendance Selector
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                val statuses = listOf(
                                    Pair("PRESENT", if (isAr) "حاضر" else "P"),
                                    Pair("LATE", if (isAr) "متأخر" else "L"),
                                    Pair("ABSENT", if (isAr) "غائب" else "A")
                                )
                                statuses.forEach { (statKey, statLabel) ->
                                    val isCurrent = selectedStudent.attendanceStatus == statKey
                                    val color = when (statKey) {
                                        "PRESENT" -> EmeraldSuccess
                                        "LATE" -> SchoolGold
                                        else -> RoseDemerit
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isCurrent) color else Color(0xFFF1F5F9),
                                        modifier = Modifier.clickable {
                                            onUpdateAttendance(selectedStudent.id, statKey)
                                        }
                                    ) {
                                        Text(
                                            text = statLabel,
                                            color = if (isCurrent) Color.White else Color(0xFF334155),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

                        // 4 Primary Staff Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Edit Official Sheet
                            Button(
                                onClick = { showEditEvaluationDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_edit_sheet_button"),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isAr) "تعديل الاستمارة" else "Edit Sheet",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Behavior Entry
                            Button(
                                onClick = { showBehaviorDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolGold),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_add_behavior_button"),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp), tint = SchoolNavy)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isAr) "تقييم سلوكي" else "Behavior",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SchoolNavy
                                    )
                                }
                            }

                            // Progress Mark
                            Button(
                                onClick = { showProgressDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolTeal),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_add_progress_button"),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Grade, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isAr) "رصد علامة" else "Record Mark",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Side Note
                            Button(
                                onClick = { showSideNoteDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_add_sidenote_button"),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isAr) "ملاحظة جانبية" else "Side Note",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Class Student Rosters - Separate Lists per Grade
        if (!showFullGridSheet) {
            when (selectedGrade) {
                "FIRST_GRADE" -> {
                    // First Grade Separate List
                    item {
                        GradeHeaderBadge(
                            title = if (isAr) "قائمة تلميذات وتلاميذ الصف الأول الأساسي (${firstGradeStudents.size} تلميذاً)"
                            else "First Grade Roster (${firstGradeStudents.size} students)",
                            subtitle = if (isAr) "معلمة المادة: أ. ريم الحلبي • اللغة العربية" else "Teacher: Ms. Reem Al-Halabi • Arabic",
                            badgeColor = SchoolTeal
                        )
                    }
                    items(firstGradeStudents) { student ->
                        StudentRosterCard(
                            student = student,
                            isSelected = student.id == selectedStudent?.id,
                            ev = allEvaluations.find { it.studentId == student.id },
                            isAr = isAr,
                            onSelect = { onSelectStudent(student.id) }
                        )
                    }
                }
                "THIRD_LEVEL" -> {
                    // Level 3 Separate List
                    item {
                        GradeHeaderBadge(
                            title = if (isAr) "قائمة أطفال الفئة الثالثة - شعبة النور (${thirdLevelStudents.size} طفلاً)"
                            else "Level 3 Roster - Kindergarten (${thirdLevelStudents.size} students)",
                            subtitle = if (isAr) "معلمة المادة: أ. غادة الرفاعي • اللغة العربية" else "Teacher: Ms. Ghada Al-Rifai • Arabic",
                            badgeColor = SchoolNavy
                        )
                    }
                    items(thirdLevelStudents) { student ->
                        StudentRosterCard(
                            student = student,
                            isSelected = student.id == selectedStudent?.id,
                            ev = allEvaluations.find { it.studentId == student.id },
                            isAr = isAr,
                            onSelect = { onSelectStudent(student.id) }
                        )
                    }
                }
                else -> {
                    // ALL: Distinct separate lists for both grades
                    item {
                        GradeHeaderBadge(
                            title = if (isAr) "قائمة الصف الأول الأساسي (${firstGradeStudents.size} تلميذاً)"
                            else "First Grade Roster (${firstGradeStudents.size} students)",
                            subtitle = if (isAr) "معلمة المادة: أ. ريم الحلبي • مدرسة منهل الحكمة الخاصة" else "Teacher: Ms. Reem Al-Halabi",
                            badgeColor = SchoolTeal
                        )
                    }
                    items(firstGradeStudents) { student ->
                        StudentRosterCard(
                            student = student,
                            isSelected = student.id == selectedStudent?.id,
                            ev = allEvaluations.find { it.studentId == student.id },
                            isAr = isAr,
                            onSelect = { onSelectStudent(student.id) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        GradeHeaderBadge(
                            title = if (isAr) "قائمة الفئة الثالثة - رياض الأطفال (${thirdLevelStudents.size} طفلاً)"
                            else "Level 3 Roster - Kindergarten (${thirdLevelStudents.size} students)",
                            subtitle = if (isAr) "معلمة المادة: أ. غادة الرفاعي • مدرسة منهل الحكمة الخاصة" else "Teacher: Ms. Ghada Al-Rifai",
                            badgeColor = SchoolNavy
                        )
                    }
                    items(thirdLevelStudents) { student ->
                        StudentRosterCard(
                            student = student,
                            isSelected = student.id == selectedStudent?.id,
                            ev = allEvaluations.find { it.studentId == student.id },
                            isAr = isAr,
                            onSelect = { onSelectStudent(student.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradeHeaderBadge(
    title: String,
    subtitle: String,
    badgeColor: Color
) {
    Surface(
        color = badgeColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, badgeColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
fun StudentRosterCard(
    student: StudentEntity,
    isSelected: Boolean,
    ev: StudentEvaluationEntity?,
    isAr: Boolean,
    onSelect: () -> Unit
) {
    val parsedColor = try {
        Color(android.graphics.Color.parseColor(student.avatarColorHex))
    } catch (e: Exception) {
        SchoolNavy
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0FDF4) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 3.dp else 1.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) EmeraldSuccess else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
            .testTag("roster_student_${student.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
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
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isAr) student.fullNameArabic else student.fullNameEnglish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val isG1 = student.gradeLevel.contains("الأول") || student.gradeLevel.contains("First")
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isG1) SchoolTeal.copy(alpha = 0.12f) else SchoolNavy.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = if (isG1) (if (isAr) "الصف الأول" else "G1") else (if (isAr) "الفئة 3" else "KG3"),
                            color = if (isG1) SchoolTeal else SchoolNavy,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }

                Text(
                    text = "${if (isAr) "القراءة:" else "Read:"} ${ev?.reading ?: "-"} • ${if (isAr) "الكتابة:" else "Write:"} ${ev?.writing ?: "-"} • ${if (isAr) "النطق:" else "Speech:"} ${ev?.pronunciation ?: "-"}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                if (ev != null && (ev.hasFrequentRestroom || ev.hasFamilySeparation || ev.specialNotes.isNotBlank())) {
                    Text(
                        text = "ملاحظة: ${ev.specialNotes.ifBlank { if (ev.hasFrequentRestroom) "دخول حمام متكرر" else "حالة خاصة" }}",
                        fontSize = 10.5.sp,
                        color = SchoolGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = EmeraldSuccess,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    isHeader: Boolean = false,
    isBold: Boolean = false,
    color: Color = Color.Unspecified,
    fontSize: androidx.compose.ui.unit.TextUnit = 11.sp
) {
    Box(
        modifier = Modifier
            .width(width)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = if (isHeader) Alignment.Center else Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = if (isHeader || isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (color != Color.Unspecified) color else if (isHeader) SchoolNavy else Color(0xFF1E293B),
            textAlign = if (isHeader) TextAlign.Center else TextAlign.Start,
            maxLines = 1
        )
    }
}

fun ratingColor(rating: String): Color {
    return when (rating) {
        "جيد" -> EmeraldSuccess
        "متوسط" -> SchoolGold
        "ضعيف" -> RoseDemerit
        else -> Color(0xFF64748B)
    }
}
