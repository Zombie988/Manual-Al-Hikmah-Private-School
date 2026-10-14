package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDemerit
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.SchoolTeal
import com.example.ui.theme.SkyInfo
import com.example.ui.viewmodel.AppLanguage

/**
 * Renders the official Manhal Al-Hikmah Private School evaluation card
 * for a student (الناحية التعليمية + الناحية السلوكية + خانة الملاحظات)
 */
@Composable
fun OfficialEvaluationCard(
    student: StudentEntity,
    evaluation: StudentEvaluationEntity?,
    language: AppLanguage,
    onAcknowledgeClick: () -> Unit,
    onEditClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.ARABIC
    val eval = evaluation ?: StudentEvaluationEntity(studentId = student.id)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .testTag("official_evaluation_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header of the sheet
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_school_logo),
                        contentDescription = "School Logo",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isAr) "استمارة التقويم المعتمدة - مدرسة منهل الحكمة الخاصة"
                            else "Official Evaluation Sheet - Manhal Al-Hikmah Private School",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SchoolNavy,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "${if (isAr) "المادة:" else "Subject:"} ${eval.subject} • ${if (isAr) "الصف:" else "Grade:"} ${eval.gradeLevel} • ${if (isAr) "المعلمة:" else "Teacher:"} ${eval.teacherName}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                if (onEditClick != null) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.testTag("edit_evaluation_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Evaluation",
                            tint = SchoolNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))

            // 1. الناحية التعليمية (Educational Aspect)
            Text(
                text = if (isAr) "١. الناحية التعليمية (القراءة، الكتابة، الخط، سرعة الكتابة، النطق):"
                else "1. Learning Skills (Reading, Writing, Handwriting, Speed, Speech):",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = SchoolTeal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EducationalSkillChip(if (isAr) "القراءة" else "Reading", eval.reading)
                EducationalSkillChip(if (isAr) "الكتابة" else "Writing", eval.writing)
                EducationalSkillChip(if (isAr) "الخط" else "Calligraphy", eval.handwriting)
                EducationalSkillChip(if (isAr) "سرعة الكتابة" else "Speed", eval.writingSpeed)
                EducationalSkillChip(if (isAr) "النطق" else "Speech", eval.pronunciation)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. الناحية السلوكية والاجتماعية (Behavioral & Social Aspect)
            Text(
                text = if (isAr) "٢. الناحية السلوكية والاجتماعية:"
                else "2. Behavioral & Social Traits:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = SchoolNavy
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BehaviorTraitChip(if (isAr) "مؤدب" else "Polite", eval.isPolite, isPositiveTrait = true)
                BehaviorTraitChip(if (isAr) "انعزالي" else "Withdrawn", eval.isWithdrawn, isPositiveTrait = false)
                BehaviorTraitChip(if (isAr) "عدواني" else "Aggressive", eval.isAggressive, isPositiveTrait = false)
                BehaviorTraitChip(if (isAr) "متردد" else "Hesitant", eval.isHesitant, isPositiveTrait = false)
                BehaviorTraitChip(if (isAr) "ملول" else "Bored/Restless", eval.isEasilyBored, isPositiveTrait = false)
                BehaviorTraitChip(if (isAr) "بطيء" else "Slow Pace", eval.isSlow, isPositiveTrait = false)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. خانة الملاحظات والحالات الخاصة (Side Notes & Special Attention)
            Text(
                text = if (isAr) "٣. خانة الملاحظات وتوجيهات المعلمة الخاصة:"
                else "3. Special Notes & Observations:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Special flags tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (eval.hasFrequentRestroom) {
                    SpecialBadge(if (isAr) "دخول حمام متكرر" else "Frequent Restroom", Color(0xFF0284C7))
                }
                if (eval.hasFamilySeparation) {
                    SpecialBadge(if (isAr) "حالة انفصال عائلي" else "Separation Case", Color(0xFFB45309))
                }
                if (eval.hasHealthCondition) {
                    SpecialBadge(if (isAr) "متابعة صحية" else "Health Follow-up", Color(0xFFDC2626))
                }
            }

            if (eval.specialNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = eval.specialNotes,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.5.sp,
                            color = Color(0xFF1E293B),
                            lineHeight = 18.sp
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

            // Parent Acknowledgment Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (eval.isAcknowledgedByParent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Acknowledged",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "تم الاطلاع والتأكيد من ولي الأمر" else "Confirmed by Parent",
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onAcknowledgeClick,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("ack_eval_button")
                    ) {
                        Icon(Icons.Default.Reply, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "تأكيد الاطلاع وكتابة رد للمعلمة" else "Acknowledge & Reply",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (eval.isAcknowledgedByParent && !eval.parentReply.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF0FDF4),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = if (isAr) "رد ولي الأمر:" else "Parent Reply:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = EmeraldSuccess
                        )
                        Text(
                            text = eval.parentReply,
                            fontSize = 12.sp,
                            color = Color(0xFF166534)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EducationalSkillChip(skillName: String, rating: String) {
    val (bg, textColor) = when (rating) {
        "جيد" -> Pair(EmeraldSuccess.copy(alpha = 0.12f), EmeraldSuccess)
        "متوسط" -> Pair(SchoolGold.copy(alpha = 0.15f), SchoolGold)
        "ضعيف" -> Pair(RoseDemerit.copy(alpha = 0.12f), RoseDemerit)
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B))
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bg,
        modifier = Modifier.border(1.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = skillName,
                fontSize = 11.sp,
                color = Color(0xFF475569),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = rating,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun BehaviorTraitChip(traitName: String, hasTrait: Boolean, isPositiveTrait: Boolean) {
    val activeColor = if (isPositiveTrait) EmeraldSuccess else RoseDemerit
    val (bg, textCol, label) = if (hasTrait) {
        Triple(activeColor.copy(alpha = 0.12f), activeColor, "$traitName: نعم")
    } else {
        Triple(Color(0xFFF8FAFC), Color(0xFF64748B), "$traitName: لا")
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        modifier = Modifier.border(1.dp, if (hasTrait) activeColor.copy(alpha = 0.3f) else Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (hasTrait) FontWeight.Bold else FontWeight.Normal,
            color = textCol,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SpecialBadge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = "• $text",
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

/**
 * Dialog for teacher to edit the official Manhal Al-Hikmah evaluation for a student
 */
@Composable
fun EditEvaluationDialog(
    student: StudentEntity,
    currentEval: StudentEvaluationEntity?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (StudentEvaluationEntity) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val initial = currentEval ?: StudentEvaluationEntity(studentId = student.id)

    var reading by remember { mutableStateOf(initial.reading) }
    var writing by remember { mutableStateOf(initial.writing) }
    var handwriting by remember { mutableStateOf(initial.handwriting) }
    var speed by remember { mutableStateOf(initial.writingSpeed) }
    var speech by remember { mutableStateOf(initial.pronunciation) }

    var isWithdrawn by remember { mutableStateOf(initial.isWithdrawn) }
    var isAggressive by remember { mutableStateOf(initial.isAggressive) }
    var isHesitant by remember { mutableStateOf(initial.isHesitant) }
    var isPolite by remember { mutableStateOf(initial.isPolite) }
    var isEasilyBored by remember { mutableStateOf(initial.isEasilyBored) }
    var isSlow by remember { mutableStateOf(initial.isSlow) }

    var hasFrequentRestroom by remember { mutableStateOf(initial.hasFrequentRestroom) }
    var hasFamilySeparation by remember { mutableStateOf(initial.hasFamilySeparation) }
    var hasHealthCondition by remember { mutableStateOf(initial.hasHealthCondition) }
    var notesText by remember { mutableStateOf(initial.specialNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAr) "تعديل تقييم: ${student.fullNameArabic}" else "Edit Evaluation: ${student.fullNameEnglish}",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section 1: الناحية التعليمية
                Text(
                    text = if (isAr) "الناحية التعليمية (جيد / متوسط / ضعيف):" else "Educational Skills:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = SchoolTeal
                )

                SkillRatingSelector(if (isAr) "القراءة" else "Reading", reading) { reading = it }
                SkillRatingSelector(if (isAr) "الكتابة" else "Writing", writing) { writing = it }
                SkillRatingSelector(if (isAr) "الخط" else "Calligraphy", handwriting) { handwriting = it }
                SkillRatingSelector(if (isAr) "سرعة الكتابة" else "Speed", speed) { speed = it }
                SkillRatingSelector(if (isAr) "النطق" else "Speech", speech) { speech = it }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // Section 2: الناحية السلوكية
                Text(
                    text = if (isAr) "الناحية السلوكية والاجتماعية (نعم / لا):" else "Behavioral & Social:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = SchoolNavy
                )

                TraitToggleRow(if (isAr) "مؤدب" else "Polite", isPolite) { isPolite = it }
                TraitToggleRow(if (isAr) "انعزالي" else "Withdrawn", isWithdrawn) { isWithdrawn = it }
                TraitToggleRow(if (isAr) "عدواني" else "Aggressive", isAggressive) { isAggressive = it }
                TraitToggleRow(if (isAr) "متردد" else "Hesitant", isHesitant) { isHesitant = it }
                TraitToggleRow(if (isAr) "ملول" else "Bored/Restless", isEasilyBored) { isEasilyBored = it }
                TraitToggleRow(if (isAr) "بطيء" else "Slow", isSlow) { isSlow = it }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // Section 3: الملاحظات والحالات الخاصة
                Text(
                    text = if (isAr) "الملاحظات والحالات الخاصة:" else "Special Notes & Cases:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF334155)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasFrequentRestroom,
                        onCheckedChange = { hasFrequentRestroom = it },
                        colors = CheckboxDefaults.colors(checkedColor = SchoolNavy)
                    )
                    Text(text = if (isAr) "دخول حمام متكرر" else "Frequent Restroom", fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasFamilySeparation,
                        onCheckedChange = { hasFamilySeparation = it },
                        colors = CheckboxDefaults.colors(checkedColor = SchoolGold)
                    )
                    Text(text = if (isAr) "حالة انفصال بين الأهل" else "Separation Case", fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasHealthCondition,
                        onCheckedChange = { hasHealthCondition = it },
                        colors = CheckboxDefaults.colors(checkedColor = RoseDemerit)
                    )
                    Text(text = if (isAr) "مرض معين / حالة صحية" else "Health Condition", fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text(if (isAr) "تفاصيل الملاحظة الخاصة" else "Special Note Details") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("edit_notes_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = initial.copy(
                        reading = reading,
                        writing = writing,
                        handwriting = handwriting,
                        writingSpeed = speed,
                        pronunciation = speech,
                        isWithdrawn = isWithdrawn,
                        isAggressive = isAggressive,
                        isHesitant = isHesitant,
                        isPolite = isPolite,
                        isEasilyBored = isEasilyBored,
                        isSlow = isSlow,
                        hasFrequentRestroom = hasFrequentRestroom,
                        hasFamilySeparation = hasFamilySeparation,
                        hasHealthCondition = hasHealthCondition,
                        specialNotes = notesText,
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(updated)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                modifier = Modifier.testTag("save_eval_changes_button")
            ) {
                Text(if (isAr) "حفظ الاستمارة" else "Save Sheet")
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
fun SkillRatingSelector(skillName: String, currentRating: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = skillName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(80.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val options = listOf("جيد", "متوسط", "ضعيف", "-")
            options.forEach { opt ->
                val isSel = currentRating == opt
                val bg = if (isSel) {
                    when (opt) {
                        "جيد" -> EmeraldSuccess
                        "متوسط" -> SchoolGold
                        "ضعيف" -> RoseDemerit
                        else -> Color(0xFF64748B)
                    }
                } else Color(0xFFF1F5F9)

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = bg,
                    modifier = Modifier.clickable { onSelect(opt) }
                ) {
                    Text(
                        text = opt,
                        color = if (isSel) Color.White else Color(0xFF334155),
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TraitToggleRow(traitName: String, value: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = traitName, fontSize = 12.sp, color = Color(0xFF1E293B))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (value) EmeraldSuccess else Color(0xFFF1F5F9),
                modifier = Modifier.clickable { onToggle(true) }
            ) {
                Text(
                    text = "نعم",
                    color = if (value) Color.White else Color(0xFF475569),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (!value) RoseDemerit.copy(alpha = 0.8f) else Color(0xFFF1F5F9),
                modifier = Modifier.clickable { onToggle(false) }
            ) {
                Text(
                    text = "لا",
                    color = if (!value) Color.White else Color(0xFF475569),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}
