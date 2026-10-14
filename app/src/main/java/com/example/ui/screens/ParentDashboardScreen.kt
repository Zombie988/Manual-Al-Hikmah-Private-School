package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BehaviorRecordEntity
import com.example.data.model.LearningProgressEntity
import com.example.data.model.SideNoteEntity
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity
import com.example.ui.components.AcknowledgeNoteDialog
import com.example.ui.components.BehaviorTraitChip
import com.example.ui.components.EducationalSkillChip
import com.example.ui.components.OfficialEvaluationCard
import com.example.ui.components.SpecialBadge
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDemerit
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.SchoolNavyDark
import com.example.ui.theme.SchoolTeal
import com.example.ui.theme.SkyInfo
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.ParentTab
import com.example.ui.viewmodel.StudentPerformanceSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ParentDashboardScreen(
    summary: StudentPerformanceSummary,
    currentTab: ParentTab,
    language: AppLanguage,
    onTabSelected: (ParentTab) -> Unit,
    onAcknowledgeNote: (noteId: Long, response: String?) -> Unit,
    onAcknowledgeEvaluation: (studentId: Long, reply: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.ARABIC
    val student = summary.student

    var noteToAcknowledge by remember { mutableStateOf<SideNoteEntity?>(null) }
    var showEvalAckDialog by remember { mutableStateOf(false) }

    if (noteToAcknowledge != null) {
        AcknowledgeNoteDialog(
            note = noteToAcknowledge!!,
            language = language,
            onDismiss = { noteToAcknowledge = null },
            onConfirm = { response ->
                onAcknowledgeNote(noteToAcknowledge!!.id, response)
                noteToAcknowledge = null
            }
        )
    }

    if (showEvalAckDialog && student != null) {
        val fakeNote = SideNoteEntity(
            studentId = student.id,
            teacherName = summary.evaluation?.teacherName ?: "أ. غادة الرفاعي",
            subject = "استمارة التقويم المعتمدة (الناحية التعليمية والسلوكية)",
            category = "ACADEMIC",
            noteText = summary.evaluation?.specialNotes.takeIf { !it.isNullOrBlank() }
                ?: "ملاحظات تقويم المهارات التعليمية والسلوكية للطفل في المدرسة."
        )
        AcknowledgeNoteDialog(
            note = fakeNote,
            language = language,
            onDismiss = { showEvalAckDialog = false },
            onConfirm = { reply ->
                onAcknowledgeEvaluation(student.id, reply)
                showEvalAckDialog = false
            }
        )
    }

    if (student == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isAr) "يرجى اختيار طالب لعرض لوحة المتابعة" else "Please select a student",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Hero Student Card
        StudentHeroBanner(student = student, summary = summary, language = language)

        // Navigation Tabs (Overview, Official Sheet, Academics, Behavior, Side Notes, Report Card)
        ParentSubTabBar(
            currentTab = currentTab,
            language = language,
            unreadNotesCount = summary.unreadNotesCount,
            onTabSelected = onTabSelected
        )

        // Content Area according to selected tab
        when (currentTab) {
            ParentTab.OVERVIEW -> ParentOverviewTab(
                student = student,
                summary = summary,
                language = language,
                onAcknowledgeClick = { noteToAcknowledge = it },
                onAcknowledgeEvalClick = { showEvalAckDialog = true },
                onViewOfficialSheet = { onTabSelected(ParentTab.OFFICIAL_SHEET) },
                onViewNotesTab = { onTabSelected(ParentTab.SIDE_NOTES) },
                onViewAcademicsTab = { onTabSelected(ParentTab.ACADEMICS) },
                onViewBehaviorTab = { onTabSelected(ParentTab.BEHAVIOR) }
            )
            ParentTab.OFFICIAL_SHEET -> ParentOfficialSheetTab(
                student = student,
                summary = summary,
                language = language,
                onAcknowledgeClick = { showEvalAckDialog = true }
            )
            ParentTab.ACADEMICS -> ParentAcademicsTab(
                progressList = summary.recentProgress,
                academicAverage = summary.academicAverage,
                language = language
            )
            ParentTab.BEHAVIOR -> ParentBehaviorTab(
                behaviorList = summary.recentBehavior,
                conductScore = summary.conductScore,
                language = language
            )
            ParentTab.SIDE_NOTES -> ParentSideNotesTab(
                sideNotes = summary.sideNotes,
                language = language,
                onAcknowledgeClick = { noteToAcknowledge = it }
            )
            ParentTab.REPORT_CARD -> ParentReportCardTab(
                student = student,
                summary = summary,
                language = language
            )
        }
    }
}

@Composable
private fun StudentHeroBanner(
    student: StudentEntity,
    summary: StudentPerformanceSummary,
    language: AppLanguage
) {
    val isAr = language == AppLanguage.ARABIC
    val avatarBg = try {
        Color(android.graphics.Color.parseColor(student.avatarColorHex))
    } catch (e: Exception) {
        SchoolNavy
    }

    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large Student Avatar
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(avatarBg),
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
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAr) student.fullNameArabic else student.fullNameEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = student.gradeLevel,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "${if (isAr) "رقم القيد:" else "ID:"} ${student.studentCode}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SchoolTeal,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Attendance Status Badge
                val (attBg, attColor, attText) = when (student.attendanceStatus) {
                    "PRESENT" -> Triple(
                        EmeraldSuccess.copy(alpha = 0.12f),
                        EmeraldSuccess,
                        if (isAr) "حاضر اليوم" else "Present"
                    )
                    "LATE" -> Triple(
                        SchoolGold.copy(alpha = 0.15f),
                        SchoolGold,
                        if (isAr) "تأخر صباحي" else "Late"
                    )
                    "EXCUSED" -> Triple(
                        SkyInfo.copy(alpha = 0.15f),
                        SkyInfo,
                        if (isAr) "غياب مبرر" else "Excused"
                    )
                    else -> Triple(
                        RoseDemerit.copy(alpha = 0.12f),
                        RoseDemerit,
                        if (isAr) "غياب غير مبرر" else "Absent"
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = attBg,
                    modifier = Modifier.border(1.dp, attColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(attColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = attText,
                            color = attColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ParentSubTabBar(
    currentTab: ParentTab,
    language: AppLanguage,
    unreadNotesCount: Int,
    onTabSelected: (ParentTab) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC

    val tabs = listOf(
        Pair(ParentTab.OVERVIEW, if (isAr) "نظرة عامة" else "Overview"),
        Pair(ParentTab.OFFICIAL_SHEET, if (isAr) "استمارة التقويم" else "Evaluation Sheet"),
        Pair(ParentTab.ACADEMICS, if (isAr) "التحصيل العلمي" else "Academics"),
        Pair(ParentTab.BEHAVIOR, if (isAr) "السلوك والمواظبة" else "Behavior"),
        Pair(ParentTab.SIDE_NOTES, if (isAr) "الملاحظات الجانبية" else "Side Notes"),
        Pair(ParentTab.REPORT_CARD, if (isAr) "بطاقة المتابعة" else "Report Card")
    )

    val selectedIndex = tabs.indexOfFirst { it.first == currentTab }.coerceAtLeast(0)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.White,
        contentColor = SchoolNavy,
        edgePadding = 12.dp,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = SchoolNavy,
                height = 3.dp
            )
        }
    ) {
        tabs.forEachIndexed { index, (tab, label) ->
            val isSelected = tab == currentTab
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.testTag("parent_subtab_${tab.name}"),
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SchoolNavy else Color(0xFF64748B),
                            fontSize = 13.sp
                        )

                        // If side notes tab and has unread, show badge counter
                        if (tab == ParentTab.SIDE_NOTES && unreadNotesCount > 0) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = RoseDemerit,
                                modifier = Modifier.size(18.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$unreadNotesCount",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ParentOverviewTab(
    student: StudentEntity,
    summary: StudentPerformanceSummary,
    language: AppLanguage,
    onAcknowledgeClick: (SideNoteEntity) -> Unit,
    onAcknowledgeEvalClick: () -> Unit,
    onViewOfficialSheet: () -> Unit,
    onViewNotesTab: () -> Unit,
    onViewAcademicsTab: () -> Unit,
    onViewBehaviorTab: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val urgentUnacknowledgedNote = summary.sideNotes.firstOrNull { it.isUrgent && !it.isAcknowledgedByParent }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Real-Time Live Ticker Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SchoolNavyDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "تحديث فوري ومباشر من مدرسة منهل الحكمة الخاصة - الصف: الفئة الثالثة"
                        else "Real-Time Direct Updates - Manhal Al-Hikmah Private School",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Feature the Official Manhal Al-Hikmah Evaluation Sheet right at the top!
        item {
            OfficialEvaluationCard(
                student = student,
                evaluation = summary.evaluation,
                language = language,
                onAcknowledgeClick = onAcknowledgeEvalClick,
                onEditClick = null
            )
        }

        // Urgent Alert Card if there's an unread urgent note
        if (urgentUnacknowledgedNote != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, RoseDemerit, RoundedCornerShape(14.dp))
                        .testTag("urgent_note_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Urgent",
                                tint = RoseDemerit,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr) "ملاحظة عاجلة من المعلمة تتطلب متابعة ولي الأمر" else "Urgent Notice from Teacher",
                                fontWeight = FontWeight.Bold,
                                color = RoseDemerit,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${urgentUnacknowledgedNote.subject}: ${urgentUnacknowledgedNote.noteText}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onAcknowledgeClick(urgentUnacknowledgedNote) },
                                colors = ButtonDefaults.buttonColors(containerColor = RoseDemerit),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("acknowledge_urgent_button")
                            ) {
                                Icon(Icons.Default.Reply, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isAr) "تأكيد الاطلاع والرد" else "Acknowledge & Reply",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4 KPI Cards Grid (Conduct, Academics, Attendance, Notes)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Conduct KPI Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onViewBehaviorTab() },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isAr) "الناحية السلوكية" else "Conduct Score",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                fontSize = 11.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SchoolGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${summary.conductScore}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (summary.conductScore >= 90) EmeraldSuccess else SchoolNavy
                                )
                            )
                            Text(
                                text = " / 100",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                        Text(
                            text = if (summary.conductScore >= 95) (if (isAr) "مؤدب - سلوك متميز" else "Exemplary")
                            else if (summary.conductScore >= 85) (if (isAr) "انضباط جيد جداً" else "Very Good")
                            else (if (isAr) "يحتاج رعاية ومتابعة" else "Attention Needed"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (summary.conductScore >= 90) EmeraldSuccess else SchoolGold
                        )
                    }
                }

                // Academics Average KPI Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onViewAcademicsTab() },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isAr) "الناحية التعليمية" else "Learning Progress",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                fontSize = 11.sp
                            )
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = SchoolNavy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f%%", summary.academicAverage),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SchoolNavy
                            )
                        )
                        Text(
                            text = if (isAr) "القراءة، الكتابة، النطق" else "Reading, Writing, Speech",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // Section: Recent Timeline Feed
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isAr) "الملاحظات اليومية والتحديثات:" else "Daily Notes & Feed:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                )
            }
        }

        // Recent side notes preview
        items(summary.sideNotes.take(3)) { note ->
            SideNoteItemCard(note = note, language = language, onAcknowledgeClick = onAcknowledgeClick)
        }
    }
}

/**
 * Dedicated Tab for viewing the official Manhal Al-Hikmah evaluation sheet
 * complete with the school's exact instructional prompt
 */
@Composable
private fun ParentOfficialSheetTab(
    student: StudentEntity,
    summary: StudentPerformanceSummary,
    language: AppLanguage,
    onAcknowledgeClick: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val eval = summary.evaluation ?: StudentEvaluationEntity(studentId = student.id)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Teacher instructions banner verbatim from sheet
        item {
            Surface(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SchoolNavy.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = SchoolNavy,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAr) "معايير التقويم الرسمية - مدرسة منهل الحكمة الخاصة"
                            else "Official Manhal Al-Hikmah Evaluation Standards",
                            fontWeight = FontWeight.Bold,
                            color = SchoolNavy,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isAr)
                            "• في جدول الناحية التعليمية: تقييم القراءة، الكتابة، الخط، سرعة الكتابة، النطق بدرجة (جيد أو متوسط أو ضعيف).\n• في جدول الناحية السلوكية والاجتماعية: تقييم (انعزالي، عدواني، متردد، مؤدب، ملول، بطيء) بكلمة (نعم أو لا).\n• في خانة الملاحظات: يكتب الطفل إن كان يعاني من مرض معين أو دخول حمام متكرر أو حالة انفصال."
                        else
                            "• Learning Aspect: Reading, Writing, Handwriting, Speed, Speech evaluated as (Good, Average, Weak).\n• Behavioral & Social: (Withdrawn, Aggressive, Hesitant, Polite, Bored, Slow) marked as (Yes/No).\n• Notes Column: Special follow-up for health conditions, frequent restroom, or family separation.",
                        fontSize = 11.5.sp,
                        color = Color(0xFF334155),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Full official card
        item {
            OfficialEvaluationCard(
                student = student,
                evaluation = eval,
                language = language,
                onAcknowledgeClick = onAcknowledgeClick
            )
        }
    }
}

@Composable
private fun ParentAcademicsTab(
    progressList: List<LearningProgressEntity>,
    academicAverage: Float,
    language: AppLanguage
) {
    val isAr = language == AppLanguage.ARABIC

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Average Header Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SchoolNavy),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isAr) "الناحية التعليمية والتحصيل الدراسي" else "Learning Progress Result",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f%%", academicAverage),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                        Text(
                            text = if (academicAverage >= 90) (if (isAr) "تقدير جيد ومستوى ممتاز" else "Good Standing")
                            else if (academicAverage >= 80) (if (isAr) "تقدير متوسط ومستقر" else "Average Standing")
                            else (if (isAr) "يحتاج متابعة وتدريب" else "Needs Attention"),
                            color = SchoolGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = if (isAr) "الاختبارات والتقييمات المدرسية:" else "Subject Assessments:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )
        }

        if (progressList.isEmpty()) {
            item {
                Text(
                    text = if (isAr) "التقييم الأكاديمي مسجل في استمارة التقويم المعتمدة." else "Evaluations recorded in official sheet.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        } else {
            items(progressList) { progress ->
                LearningProgressItemCard(progress = progress, language = language)
            }
        }
    }
}

@Composable
private fun LearningProgressItemCard(
    progress: LearningProgressEntity,
    language: AppLanguage
) {
    val isAr = language == AppLanguage.ARABIC
    val perc = progress.percentage
    val progressColor = when {
        perc >= 90f -> EmeraldSuccess
        perc >= 80f -> SchoolNavy
        perc >= 70f -> SchoolGold
        else -> RoseDemerit
    }

    val dateStr = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(progress.timestamp))

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isAr) progress.subjectTitleArabic else progress.subjectTitleEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Text(
                        text = "${progress.assessmentTitleArabic} • $dateStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = progressColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${progress.score.toInt()} / ${progress.maxScore.toInt()}",
                        color = progressColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { (perc / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = progressColor,
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${if (isAr) "موضوع التقييم:" else "Topic:"} ${if (isAr) progress.topicArabic else progress.topicEnglish}",
                fontSize = 12.sp,
                color = Color(0xFF334155),
                fontWeight = FontWeight.Medium
            )

            if (progress.teacherNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💬 ${progress.teacherNotes}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ParentBehaviorTab(
    behaviorList: List<BehaviorRecordEntity>,
    conductScore: Int,
    language: AppLanguage
) {
    val isAr = language == AppLanguage.ARABIC

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Conduct Gauge Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAr) "الناحية السلوكية والاجتماعية" else "Behavioral & Social Progress",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SchoolNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$conductScore",
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp,
                            color = if (conductScore >= 90) EmeraldSuccess else SchoolNavy
                        )
                        Text(
                            text = " / 100",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (conductScore / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (conductScore >= 90) EmeraldSuccess else SchoolGold,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isAr) "يتضمن مؤشرات: مؤدب، انعزالي، عدواني، متردد، ملول، بطيء، والالتزام الصفي."
                        else "Includes traits: Polite, Withdrawn, Aggressive, Hesitant, Restless, Slow, and classroom discipline.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Text(
                text = if (isAr) "سجل المواقف والملاحظات السلوكية:" else "Behavior Event Logs:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )
        }

        if (behaviorList.isEmpty()) {
            item {
                Text(
                    text = if (isAr) "السلوك والسمات مسجلة في استمارة التقويم الرسمية." else "Traits tracked in official evaluation sheet.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        } else {
            items(behaviorList) { record ->
                BehaviorRecordItemCard(record = record, language = language)
            }
        }
    }
}

@Composable
private fun BehaviorRecordItemCard(
    record: BehaviorRecordEntity,
    language: AppLanguage
) {
    val isAr = language == AppLanguage.ARABIC
    val isPositive = record.pointsDelta > 0
    val deltaColor = if (isPositive) EmeraldSuccess else RoseDemerit
    val dateStr = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()).format(Date(record.timestamp))

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(deltaColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPositive) Icons.Default.Star else Icons.Default.Warning,
                            contentDescription = null,
                            tint = deltaColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isAr) record.titleArabic else record.titleEnglish,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        )
                        Text(
                            text = "${record.teacherName} • $dateStr",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = deltaColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isPositive) "+${record.pointsDelta}" else "${record.pointsDelta}",
                        color = deltaColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (record.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = record.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun ParentSideNotesTab(
    sideNotes: List<SideNoteEntity>,
    language: AppLanguage,
    onAcknowledgeClick: (SideNoteEntity) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = SchoolNavy,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isAr) "دفتر الملاحظات الجانبية والحالات الخاصة" else "Special Side Notes & Cases",
                            fontWeight = FontWeight.Bold,
                            color = SchoolNavy,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (isAr) "ملاحظات المعلمة عن دخول الحمام، الحالات الصحية، وحالات الانفصال مع إمكانية تأكيد الاطلاع المباشر."
                            else "Observations on restroom habits, health, or family cases with instant parent acknowledgment.",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }
        }

        if (sideNotes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isAr) "لا توجد ملاحظات جانبية إضافية حالياً." else "No side notes recorded yet.",
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(sideNotes) { note ->
                SideNoteItemCard(
                    note = note,
                    language = language,
                    onAcknowledgeClick = onAcknowledgeClick
                )
            }
        }
    }
}

@Composable
fun SideNoteItemCard(
    note: SideNoteEntity,
    language: AppLanguage,
    onAcknowledgeClick: (SideNoteEntity) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val dateStr = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()).format(Date(note.timestamp))

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (note.isUrgent && !note.isAcknowledgedByParent) Color(0xFFFEF2F2) else Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (note.isUrgent && !note.isAcknowledgedByParent) 1.5.dp else 1.dp,
                color = if (note.isUrgent && !note.isAcknowledgedByParent) RoseDemerit else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("side_note_card_${note.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Category Badge, Urgency, Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SchoolNavy.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = note.subject,
                            color = SchoolNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (note.isUrgent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RoseDemerit
                        ) {
                            Text(
                                text = if (isAr) "عاجل" else "Urgent",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Note Text
            Text(
                text = note.noteText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF1E293B)
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "— ${note.teacherName}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))

            // Parent Acknowledgment Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (note.isAcknowledgedByParent) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Acknowledged",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "تم الاطلاع والتأكيد من ولي الأمر" else "Acknowledged by Parent",
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Button(
                        onClick = { onAcknowledgeClick(note) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("ack_button_${note.id}")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "تأكيد الاطلاع وكتابة رد" else "Acknowledge & Reply",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Display Parent's Reply if any
            if (note.isAcknowledgedByParent && !note.parentResponse.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF0FDF4),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = if (isAr) "رد ولي الأمر:" else "Parent Response:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = EmeraldSuccess
                        )
                        Text(
                            text = note.parentResponse,
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
private fun ParentReportCardTab(
    student: StudentEntity,
    summary: StudentPerformanceSummary,
    language: AppLanguage
) {
    val isAr = language == AppLanguage.ARABIC
    val eval = summary.evaluation ?: StudentEvaluationEntity(studentId = student.id)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    // Report Card Official Header
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_school_logo),
                            contentDescription = "Manhal Al-Hikmah Logo",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isAr) "الجمهورية العربية السورية - وزارة التربية" else "Syrian Arab Republic - Ministry of Education",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = if (isAr) "مدرسة منهل الحكمة الخاصة" else "Manhal Al-Hikmah Private School",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SchoolNavy
                        )
                        Text(
                            text = if (isAr) "بطاقة التقويم والمتابعة التربوية الشاملة" else "Comprehensive Student Progress & Evaluation",
                            fontSize = 13.sp,
                            color = SchoolGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                    // Student Meta Data
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${if (isAr) "اسم التلميذ:" else "Student Name:"} ${if (isAr) student.fullNameArabic else student.fullNameEnglish}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "${if (isAr) "رقم القيد:" else "Code:"} ${student.studentCode}",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Text(
                            text = "${if (isAr) "المادة والصف:" else "Subject & Grade:"} ${eval.subject} • ${eval.gradeLevel}",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = "${if (isAr) "المعلمة المسؤولة:" else "Teacher:"} ${eval.teacherName}",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                    // Section 1: الناحية التعليمية
                    Text(
                        text = if (isAr) "١. الناحية التعليمية (التقويم: جيد / متوسط / ضعيف):" else "1. Educational Evaluation:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SchoolNavy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        EducationalSkillChip(if (isAr) "القراءة" else "Reading", eval.reading)
                        EducationalSkillChip(if (isAr) "الكتابة" else "Writing", eval.writing)
                        EducationalSkillChip(if (isAr) "الخط" else "Calligraphy", eval.handwriting)
                        EducationalSkillChip(if (isAr) "سرعة الكتابة" else "Speed", eval.writingSpeed)
                        EducationalSkillChip(if (isAr) "النطق" else "Speech", eval.pronunciation)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Section 2: الناحية السلوكية والاجتماعية
                    Text(
                        text = if (isAr) "٢. الناحية السلوكية والاجتماعية (التقييم: نعم / لا):" else "2. Behavioral Traits (Yes / No):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SchoolTeal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BehaviorTraitChip(if (isAr) "مؤدب" else "Polite", eval.isPolite, isPositiveTrait = true)
                        BehaviorTraitChip(if (isAr) "انعزالي" else "Withdrawn", eval.isWithdrawn, isPositiveTrait = false)
                        BehaviorTraitChip(if (isAr) "عدواني" else "Aggressive", eval.isAggressive, isPositiveTrait = false)
                        BehaviorTraitChip(if (isAr) "متردد" else "Hesitant", eval.isHesitant, isPositiveTrait = false)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Section 3: الملاحظات والحالات الخاصة
                    Text(
                        text = if (isAr) "٣. خانة الملاحظات والحالات الخاصة والمتابعة:" else "3. Special Notes & Conditions:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (eval.hasFrequentRestroom) {
                                Text(text = "• ${if (isAr) "دخول حمام متكرر: يرجى المتابعة الصحية" else "Frequent Restroom Visits"}", color = Color(0xFF0284C7), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            if (eval.hasFamilySeparation) {
                                Text(text = "• ${if (isAr) "حالة انفصال عائلي: رعاية نفسية خاصة" else "Family Separation Case"}", color = Color(0xFFB45309), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            if (eval.specialNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = eval.specialNotes, fontSize = 12.sp, color = Color(0xFF1E293B))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Signatures Footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isAr) "معلمة المادة" else "Teacher",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(text = eval.teacherName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isAr) "إدارة مدرسة منهل الحكمة" else "School Administration",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(text = "الختم والتوقيع الرسمي", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
