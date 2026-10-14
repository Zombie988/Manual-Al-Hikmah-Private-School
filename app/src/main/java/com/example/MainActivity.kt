package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddStudentDialog
import com.example.ui.components.SchoolTopBar
import com.example.ui.components.StudentSelectorStrip
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.TeacherPortalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.ParentTab
import com.example.ui.viewmodel.SchoolViewModel
import com.example.ui.viewmodel.UserRole

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SchoolApp()
            }
        }
    }
}

@Composable
fun SchoolApp(
    viewModel: SchoolViewModel = viewModel()
) {
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val parentTab by viewModel.parentTab.collectAsStateWithLifecycle()
    val allStudents by viewModel.allStudents.collectAsStateWithLifecycle()
    val allEvaluations by viewModel.allEvaluations.collectAsStateWithLifecycle()
    val summary by viewModel.studentSummary.collectAsStateWithLifecycle()
    val currentStudent by viewModel.currentStudentFlow.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()

    var showAddStudentDialog by remember { mutableStateOf(false) }

    // Handle back button: if in Teacher portal or in a sub-tab, return to parent overview
    BackHandler(enabled = userRole == UserRole.TEACHER || parentTab != ParentTab.OVERVIEW) {
        if (userRole == UserRole.TEACHER) {
            viewModel.setRole(UserRole.PARENT)
        } else if (parentTab != ParentTab.OVERVIEW) {
            viewModel.setParentTab(ParentTab.OVERVIEW)
        }
    }

    if (showAddStudentDialog) {
        AddStudentDialog(
            language = language,
            onDismiss = { showAddStudentDialog = false },
            onSubmit = { fullNameAr, fullNameEn, grade, guardian, phone ->
                viewModel.addNewStudent(fullNameAr, fullNameEn, grade, guardian, phone)
            }
        )
    }

    val layoutDirection = if (language == AppLanguage.ARABIC) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column {
                    SchoolTopBar(
                        currentRole = userRole,
                        language = language,
                        onRoleChanged = { viewModel.setRole(it) },
                        onToggleLanguage = { viewModel.toggleLanguage() }
                    )

                    StudentSelectorStrip(
                        students = allStudents,
                        selectedStudentId = currentStudent?.id,
                        selectedGrade = selectedGrade,
                        language = language,
                        onSelectGrade = { viewModel.setSelectedGrade(it) },
                        onSelectStudent = { viewModel.selectStudent(it) },
                        onAddNewStudentClick = if (userRole == UserRole.TEACHER) {
                            { showAddStudentDialog = true }
                        } else null
                    )
                }
            }
        ) { innerPadding ->
            when (userRole) {
                UserRole.PARENT -> {
                    ParentDashboardScreen(
                        summary = summary,
                        currentTab = parentTab,
                        language = language,
                        onTabSelected = { viewModel.setParentTab(it) },
                        onAcknowledgeNote = { noteId, response ->
                            viewModel.acknowledgeSideNote(noteId, response)
                        },
                        onAcknowledgeEvaluation = { studentId, reply ->
                            viewModel.acknowledgeEvaluation(studentId, reply)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                UserRole.TEACHER -> {
                    TeacherPortalScreen(
                        students = allStudents,
                        allEvaluations = allEvaluations,
                        selectedStudent = currentStudent,
                        summary = summary,
                        language = language,
                        selectedGrade = selectedGrade,
                        onSelectGrade = { viewModel.setSelectedGrade(it) },
                        onSelectStudent = { viewModel.selectStudent(it) },
                        onUpdateAttendance = { studentId, status ->
                            viewModel.updateAttendance(studentId, status)
                        },
                        onSaveEvaluation = { eval ->
                            viewModel.saveOrUpdateEvaluation(eval)
                        },
                        onRecordBehavior = { sId, points, cat, titleAr, titleEn, note, teacher ->
                            viewModel.recordBehavior(sId, points, cat, titleAr, titleEn, note, teacher)
                        },
                        onRecordProgress = { sId, code, subAr, subEn, aType, aTitle, score, maxScore, topAr, topEn, notes ->
                            viewModel.recordProgress(sId, code, subAr, subEn, aType, aTitle, score, maxScore, topAr, topEn, notes)
                        },
                        onAddSideNote = { sId, teacher, subject, category, text, isUrgent ->
                            viewModel.addSideNote(sId, teacher, subject, category, text, isUrgent)
                        },
                        onAddNewStudent = { arName, enName, grade, guardian, phone ->
                            viewModel.addNewStudent(arName, enName, grade, guardian, phone)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
