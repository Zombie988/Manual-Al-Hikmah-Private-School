package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SideNoteEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDemerit
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.viewmodel.AppLanguage

@Composable
fun AddBehaviorDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (points: Int, category: String, titleAr: String, titleEn: String, note: String, teacher: String) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    var selectedPoints by remember { mutableStateOf(5) }
    var selectedCategory by remember { mutableStateOf("PARTICIPATION") }
    var titleText by remember { mutableStateOf(if (isAr) "مشاركة صفية متألقة" else "Active Class Participation") }
    var noteText by remember { mutableStateOf(if (isAr) "أظهر تفاعلاً إيجابياً ومبادرة ممتازة خلال الحصة." else "Demonstrated positive interaction and great initiative.") }
    var teacherName by remember { mutableStateOf(if (isAr) "أ. المشرف التربوي" else "Teacher / Mentor") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAr) "تسجيل تقييم سلوكي جديد" else "Record Behavior Evaluation",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Points selector
                Text(
                    text = if (isAr) "النقاط (مكافأة أو حسم):" else "Points (Merit or Demerit):",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val pointsOptions = listOf(+10, +5, +3, -3, -5, -10)
                    pointsOptions.forEach { pts ->
                        val isSel = selectedPoints == pts
                        val isPositive = pts > 0
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isSel && isPositive -> EmeraldSuccess
                                isSel && !isPositive -> RoseDemerit
                                isPositive -> EmeraldSuccess.copy(alpha = 0.15f)
                                else -> RoseDemerit.copy(alpha = 0.15f)
                            },
                            modifier = Modifier
                                .clickable { selectedPoints = pts }
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = (if (pts > 0) "+$pts" else "$pts"),
                                color = if (isSel) Color.White else if (isPositive) EmeraldSuccess else RoseDemerit,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Preset Category Chips
                Text(
                    text = if (isAr) "نوع السلوك / الملاحظة:" else "Behavior Category:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                val categories = listOf(
                    Triple("PARTICIPATION", "مشاركة صفية", "Class Participation"),
                    Triple("DISCIPLINE", "انضباط وهدوء", "Quiet Discipline"),
                    Triple("HOMEWORK", "واجب مدرسي متقن", "Homework Diligence"),
                    Triple("LEADERSHIP", "مبادرة وقيادة", "Leadership & Initiative"),
                    Triple("TARDINESS", "تأخر صباحي", "Morning Tardiness"),
                    Triple("DISTRACTION", "تشويش في الحصة", "Class Distraction")
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { (catKey, arLabel, enLabel) ->
                        val isSel = selectedCategory == catKey
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) SchoolNavy else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable {
                                selectedCategory = catKey
                                titleText = if (isAr) arLabel else enLabel
                            }
                        ) {
                            Text(
                                text = if (isAr) arLabel else enLabel,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text(if (isAr) "عنوان التقييم" else "Title") },
                    modifier = Modifier.fillMaxWidth().testTag("behavior_title_input")
                )

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text(if (isAr) "تفاصيل الملاحظة السلوكية" else "Details / Notes") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("behavior_note_input")
                )

                OutlinedTextField(
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = { Text(if (isAr) "اسم المعلم / الموجه" else "Staff / Teacher Name") },
                    modifier = Modifier.fillMaxWidth().testTag("behavior_teacher_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val enTitle = if (isAr) "Behavior Assessment: $titleText" else titleText
                    val arTitle = if (isAr) titleText else "تقييم سلوكي: $titleText"
                    onSubmit(selectedPoints, selectedCategory, arTitle, enTitle, noteText, teacherName)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                modifier = Modifier.testTag("submit_behavior_button")
            ) {
                Text(if (isAr) "حفظ التقييم" else "Save Assessment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isAr) "إلغاء" else "Cancel")
            }
        }
    )
}

@Composable
fun AddProgressDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
        subjectCode: String,
        subjectAr: String,
        subjectEn: String,
        assessmentType: String,
        assessmentTitleAr: String,
        score: Float,
        maxScore: Float,
        topicAr: String,
        topicEn: String,
        notes: String
    ) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC

    val subjects = listOf(
        Triple("ARABIC", "اللغة العربية", "Arabic"),
        Triple("MATH", "الرياضيات", "Mathematics"),
        Triple("SCIENCE", "الفيزياء والكيمياء والعلوم", "Sciences & Physics"),
        Triple("ENGLISH", "اللغة الإنكليزية", "English"),
        Triple("FRENCH", "اللغة الفرنسية", "French"),
        Triple("INFORMATICS", "المعلوماتية والبرمجة", "Informatics"),
        Triple("HISTORY_GEO", "التاريخ والجغرافيا", "Social Studies")
    )

    val assessments = listOf(
        Pair("QUIZ_1", if (isAr) "المذاكرة الأولى" else "Quiz 1"),
        Pair("QUIZ_2", if (isAr) "المذاكرة الثانية" else "Quiz 2"),
        Pair("ACTIVITY", if (isAr) "نشاط وتطبيق عملي" else "Practical Activity"),
        Pair("ORAL", if (isAr) "تسميع شفهي" else "Oral Recitation"),
        Pair("MIDTERM", if (isAr) "امتحان نصفي" else "Midterm Exam"),
        Pair("FINAL", if (isAr) "امتحان فصلي" else "Final Exam")
    )

    var selectedSubject by remember { mutableStateOf(subjects[0]) }
    var selectedAssessment by remember { mutableStateOf(assessments[0]) }
    var scoreInput by remember { mutableStateOf("95") }
    var maxScoreInput by remember { mutableStateOf("100") }
    var topicInput by remember { mutableStateOf(if (isAr) "المتطابقات والتحليل الجبري" else "Algebraic Identities") }
    var notesInput by remember { mutableStateOf(if (isAr) "أداء ممتاز وحل دقيق للتمارين." else "Outstanding performance and accurate steps.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAr) "تسجيل علامة اختبار / تقدم دراسي" else "Record Assessment / Academic Mark",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Subject Chips
                Text(
                    text = if (isAr) "اختر المادة الدراسية:" else "Select Subject:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjects.forEach { subj ->
                        val isSel = selectedSubject.first == subj.first
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) SchoolNavy else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { selectedSubject = subj }
                        ) {
                            Text(
                                text = if (isAr) subj.second else subj.third,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Assessment Type Chips
                Text(
                    text = if (isAr) "نوع التقييم:" else "Assessment Type:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    assessments.forEach { ass ->
                        val isSel = selectedAssessment.first == ass.first
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) SchoolGold else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { selectedAssessment = ass }
                        ) {
                            Text(
                                text = ass.second,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = scoreInput,
                        onValueChange = { scoreInput = it },
                        label = { Text(if (isAr) "العلامة المستحقة" else "Score") },
                        modifier = Modifier.weight(1f).testTag("score_input")
                    )
                    OutlinedTextField(
                        value = maxScoreInput,
                        onValueChange = { maxScoreInput = it },
                        label = { Text(if (isAr) "الدرجة العظمى" else "Max Score") },
                        modifier = Modifier.weight(1f).testTag("max_score_input")
                    )
                }

                OutlinedTextField(
                    value = topicInput,
                    onValueChange = { topicInput = it },
                    label = { Text(if (isAr) "موضوع الاختبار / المحتوى" else "Topic / Tested Area") },
                    modifier = Modifier.fillMaxWidth().testTag("topic_input")
                )

                OutlinedTextField(
                    value = notesInput,
                    onValueChange = { notesInput = it },
                    label = { Text(if (isAr) "ملاحظة المعلم والتوجيه" else "Teacher Feedback") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("assessment_notes_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val s = scoreInput.toFloatOrNull() ?: 0f
                    val m = maxScoreInput.toFloatOrNull() ?: 100f
                    val arTopic = if (isAr) topicInput else "موضوع: $topicInput"
                    val enTopic = if (isAr) "Topic: $topicInput" else topicInput
                    onSubmit(
                        selectedSubject.first,
                        selectedSubject.second,
                        selectedSubject.third,
                        selectedAssessment.first,
                        selectedAssessment.second,
                        s,
                        m,
                        arTopic,
                        enTopic,
                        notesInput
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                modifier = Modifier.testTag("submit_progress_button")
            ) {
                Text(if (isAr) "تسجيل العلامة" else "Save Score")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isAr) "إلغاء" else "Cancel")
            }
        }
    )
}

@Composable
fun AddSideNoteDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (teacher: String, subject: String, category: String, text: String, isUrgent: Boolean) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    var teacherInput by remember { mutableStateOf(if (isAr) "أ. المشرف التربوي" else "Teacher / Mentor") }
    var subjectInput by remember { mutableStateOf(if (isAr) "المتابعة اليومية" else "Daily Follow-up") }
    var noteInput by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("ACADEMIC") }

    val categories = listOf(
        Pair("ACADEMIC", if (isAr) "أكاديمية ودراسية" else "Academic"),
        Pair("BEHAVIORAL", if (isAr) "سلوكية وتربوية" else "Behavioral"),
        Pair("ADMINISTRATIVE", if (isAr) "إدارية ومواعيد" else "Administrative"),
        Pair("HEALTH", if (isAr) "صحية وبدنية" else "Health"),
        Pair("PRAISE", if (isAr) "إشادة وتشجيع" else "Praise")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAr) "تدوين ملاحظة جانبية للمتابعة" else "Write Side Note for Parent",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isAr) "تصنيف الملاحظة الجانبية:" else "Note Category:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { (catKey, catLabel) ->
                        val isSel = selectedCategory == catKey
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) SchoolNavy else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { selectedCategory = catKey }
                        ) {
                            Text(
                                text = catLabel,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = subjectInput,
                    onValueChange = { subjectInput = it },
                    label = { Text(if (isAr) "المادة / الموضوع" else "Subject / Topic") },
                    modifier = Modifier.fillMaxWidth().testTag("side_note_subject_input")
                )

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text(if (isAr) "نص الملاحظة الموجهة لولي الأمر" else "Side Note Content") },
                    placeholder = { Text(if (isAr) "اكتب الملاحظة هنا..." else "Enter note details...") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth().testTag("side_note_text_input")
                )

                OutlinedTextField(
                    value = teacherInput,
                    onValueChange = { teacherInput = it },
                    label = { Text(if (isAr) "اسم الكاتب / المعلم" else "Teacher Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAr) "ملاحظة عاجلة" else "Urgent Note",
                            fontWeight = FontWeight.Bold,
                            color = if (isUrgent) RoseDemerit else Color(0xFF1E293B)
                        )
                        Text(
                            text = if (isAr) "تنبيه فوري يظهر في أعلى لوحة تحكم ولي الأمر" else "Highlights prominently in parent portal",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = RoseDemerit)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (noteInput.isNotBlank()) {
                        onSubmit(teacherInput, subjectInput, selectedCategory, noteInput, isUrgent)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                modifier = Modifier.testTag("submit_side_note_button")
            ) {
                Text(if (isAr) "إرسال لولي الأمر" else "Post Note")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isAr) "إلغاء" else "Cancel")
            }
        }
    )
}

@Composable
fun AcknowledgeNoteDialog(
    note: SideNoteEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (response: String?) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    var responseText by remember {
        mutableStateOf(if (isAr) "تم الاطلاع وسنتابع مع الطالب، شكراً لجهودكم." else "Acknowledged and will follow up, thank you.")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAr) "تأكيد الاطلاع والرد على الملاحظة" else "Acknowledge & Respond to Note",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${note.subject} - ${note.teacherName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SchoolNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = note.noteText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF334155)
                        )
                    }
                }

                Text(
                    text = if (isAr) "رسالة رد أو تعليق من ولي الأمر (اختياري):" else "Parent Reply / Follow-up Note (Optional):",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = responseText,
                    onValueChange = { responseText = it },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("parent_reply_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(responseText.ifBlank { null })
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                modifier = Modifier.testTag("confirm_acknowledge_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isAr) "تأكيد الاطلاع والرد" else "Confirm & Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isAr) "إلغاء" else "Cancel")
            }
        }
    )
}

@Composable
fun AddStudentDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (fullNameAr: String, fullNameEn: String, grade: String, guardian: String, phone: String) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    var nameAr by remember { mutableStateOf("") }
    var nameEn by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf(if (isAr) "الصف الأول (الشعبة الأولى)" else "First Grade (Section 1)") }
    var guardian by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+963 9") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAr) "إضافة طالب جديد للسجل المدرسي" else "Add New Student to School Roster",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nameAr,
                    onValueChange = { nameAr = it },
                    label = { Text(if (isAr) "الاسم الثلاثي بالعربية" else "Arabic Full Name") },
                    modifier = Modifier.fillMaxWidth().testTag("student_name_ar_input")
                )
                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(if (isAr) "الاسم بالإنكليزية" else "English Full Name") },
                    modifier = Modifier.fillMaxWidth().testTag("student_name_en_input")
                )

                Text(
                    text = if (isAr) "اختر الصف الدراسي:" else "Select Grade Level:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color(0xFF475569)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val gPresets = listOf(
                        Pair(if (isAr) "الصف الأول (الشعبة الأولى)" else "First Grade (Section 1)", if (isAr) "الصف الأول" else "1st Grade"),
                        Pair(if (isAr) "الفئة الثالثة (شعبة النور)" else "Level 3 (Al-Noor)", if (isAr) "الفئة الثالثة" else "KG3")
                    )
                    gPresets.forEach { (fullGrade, label) ->
                        val isSelected = grade == fullGrade
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) SchoolNavy else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { grade = fullGrade }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = grade,
                    onValueChange = { grade = it },
                    label = { Text(if (isAr) "الصف والشعبة المحدد" else "Grade & Section") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = guardian,
                    onValueChange = { guardian = it },
                    label = { Text(if (isAr) "اسم ولي الأمر" else "Guardian Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(if (isAr) "هاتف ولي الأمر" else "Guardian Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameAr.isNotBlank() || nameEn.isNotBlank()) {
                        val finalAr = nameAr.ifBlank { nameEn }
                        val finalEn = nameEn.ifBlank { nameAr }
                        onSubmit(finalAr, finalEn, grade, guardian.ifBlank { "ولي الأمر" }, phone)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                modifier = Modifier.testTag("confirm_add_student_button")
            ) {
                Text(if (isAr) "تسجيل الطالب" else "Enroll Student")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isAr) "إلغاء" else "Cancel")
            }
        }
    )
}
