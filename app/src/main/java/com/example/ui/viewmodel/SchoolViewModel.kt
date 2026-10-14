package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SchoolDatabase
import com.example.data.model.BehaviorRecordEntity
import com.example.data.model.LearningProgressEntity
import com.example.data.model.SideNoteEntity
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    PARENT,
    TEACHER
}

enum class ParentTab {
    OVERVIEW,
    OFFICIAL_SHEET, // استمارة التقييم الرسمية (منهل الحكمة)
    ACADEMICS,
    BEHAVIOR,
    SIDE_NOTES,
    REPORT_CARD
}

enum class AppLanguage {
    ARABIC,
    ENGLISH
}

data class StudentPerformanceSummary(
    val student: StudentEntity?,
    val evaluation: StudentEvaluationEntity? = null,
    val conductScore: Int = 100,
    val academicAverage: Float = 0f,
    val totalQuizzes: Int = 0,
    val positiveMeritsCount: Int = 0,
    val demeritsCount: Int = 0,
    val unreadNotesCount: Int = 0,
    val recentBehavior: List<BehaviorRecordEntity> = emptyList(),
    val recentProgress: List<LearningProgressEntity> = emptyList(),
    val sideNotes: List<SideNoteEntity> = emptyList()
)

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository

    init {
        val db = SchoolDatabase.getDatabase(application)
        repository = SchoolRepository(db.schoolDao())
        viewModelScope.launch {
            repository.initialize()
        }
    }

    private val _userRole = MutableStateFlow(UserRole.PARENT)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _parentTab = MutableStateFlow(ParentTab.OVERVIEW)
    val parentTab: StateFlow<ParentTab> = _parentTab.asStateFlow()

    private val _selectedStudentId = MutableStateFlow<Long?>(null)
    val selectedStudentId: StateFlow<Long?> = _selectedStudentId.asStateFlow()

    // Selected Grade Filter: "FIRST_GRADE", "THIRD_LEVEL", or "ALL"
    private val _selectedGrade = MutableStateFlow<String>("FIRST_GRADE")
    val selectedGrade: StateFlow<String> = _selectedGrade.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.ARABIC)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    val allStudents: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Separate Grade Lists for Ease-of-Use
    val firstGradeStudents: StateFlow<List<StudentEntity>> = allStudents.map { list ->
        list.filter { it.gradeLevel.contains("الأول") || it.gradeLevel.contains("First") }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val thirdLevelStudents: StateFlow<List<StudentEntity>> = allStudents.map { list ->
        list.filter { it.gradeLevel.contains("الثالثة") || it.gradeLevel.contains("Third") || it.gradeLevel.contains("KG3") }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Students dynamically filtered by selectedGrade
    val displayedStudents: StateFlow<List<StudentEntity>> = combine(
        allStudents,
        _selectedGrade
    ) { students, grade ->
        when (grade) {
            "FIRST_GRADE" -> students.filter { it.gradeLevel.contains("الأول") || it.gradeLevel.contains("First") }
            "THIRD_LEVEL" -> students.filter { it.gradeLevel.contains("الثالثة") || it.gradeLevel.contains("Third") || it.gradeLevel.contains("KG3") }
            else -> students
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEvaluations: StateFlow<List<StudentEvaluationEntity>> = repository.allEvaluations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSideNotes: StateFlow<List<SideNoteEntity>> = repository.allSideNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Student Details & Performance
    val currentStudentFlow: StateFlow<StudentEntity?> = combine(
        allStudents,
        _selectedStudentId
    ) { students, selectedId ->
        if (students.isEmpty()) return@combine null
        val targetId = selectedId ?: students.first().id
        students.find { it.id == targetId } ?: students.first()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentEvaluation: StateFlow<StudentEvaluationEntity?> = currentStudentFlow.flatMapLatest { student ->
        if (student == null) flowOf(null)
        else repository.getEvaluationForStudent(student.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentBehaviorRecords: StateFlow<List<BehaviorRecordEntity>> = currentStudentFlow.flatMapLatest { student ->
        if (student == null) flowOf(emptyList())
        else repository.getBehaviorRecords(student.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentLearningProgress: StateFlow<List<LearningProgressEntity>> = currentStudentFlow.flatMapLatest { student ->
        if (student == null) flowOf(emptyList())
        else repository.getLearningProgress(student.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentSideNotes: StateFlow<List<SideNoteEntity>> = currentStudentFlow.flatMapLatest { student ->
        if (student == null) flowOf(emptyList())
        else repository.getSideNotes(student.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentSummary: StateFlow<StudentPerformanceSummary> = combine(
        currentStudentFlow,
        currentEvaluation,
        currentBehaviorRecords,
        currentLearningProgress,
        currentSideNotes
    ) { student, evaluation, behaviors, progressList, notes ->
        var conductBase = 100
        // Calculate conduct with adjustments from official evaluation:
        if (evaluation != null) {
            if (evaluation.isWithdrawn) conductBase -= 4
            if (evaluation.isAggressive) conductBase -= 12
            if (evaluation.isHesitant) conductBase -= 3
            if (evaluation.isEasilyBored) conductBase -= 4
            if (evaluation.isSlow) conductBase -= 2
            if (evaluation.isPolite) conductBase += 5
        }
        val deltas = behaviors.sumOf { it.pointsDelta }
        val finalConduct = (conductBase + deltas).coerceIn(0, 100)

        // Calculate academic score:
        var totalPoints = 0f
        var count = 0
        if (evaluation != null) {
            listOf(evaluation.reading, evaluation.writing, evaluation.handwriting, evaluation.writingSpeed, evaluation.pronunciation).forEach { rating ->
                when (rating) {
                    "جيد" -> { totalPoints += 95f; count++ }
                    "متوسط" -> { totalPoints += 78f; count++ }
                    "ضعيف" -> { totalPoints += 55f; count++ }
                }
            }
        }
        progressList.forEach {
            totalPoints += it.percentage
            count++
        }
        val academicAvg = if (count > 0) totalPoints / count else 90f

        val positiveCount = behaviors.count { it.pointsDelta > 0 } + (if (evaluation?.isPolite == true) 1 else 0)
        val demeritCount = behaviors.count { it.pointsDelta < 0 } +
                (if (evaluation?.isAggressive == true) 1 else 0) +
                (if (evaluation?.isEasilyBored == true) 1 else 0)

        val unread = notes.count { !it.isAcknowledgedByParent } + (if (evaluation != null && !evaluation.isAcknowledgedByParent && evaluation.specialNotes.isNotBlank()) 1 else 0)

        StudentPerformanceSummary(
            student = student,
            evaluation = evaluation,
            conductScore = finalConduct,
            academicAverage = academicAvg,
            totalQuizzes = progressList.size + (if (evaluation != null) 5 else 0),
            positiveMeritsCount = positiveCount,
            demeritsCount = demeritCount,
            unreadNotesCount = unread,
            recentBehavior = behaviors,
            recentProgress = progressList,
            sideNotes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudentPerformanceSummary(null))

    fun setRole(role: UserRole) {
        _userRole.value = role
    }

    fun setParentTab(tab: ParentTab) {
        _parentTab.value = tab
    }

    fun selectStudent(studentId: Long) {
        _selectedStudentId.value = studentId
    }

    fun setSelectedGrade(grade: String) {
        _selectedGrade.value = grade
        // When changing grade, if current student doesn't belong to the newly selected grade, auto-select first student in that grade
        val current = currentStudentFlow.value
        val all = allStudents.value
        val matchesCurrent = when (grade) {
            "FIRST_GRADE" -> current?.gradeLevel?.let { it.contains("الأول") || it.contains("First") } ?: false
            "THIRD_LEVEL" -> current?.gradeLevel?.let { it.contains("الثالثة") || it.contains("Third") || it.contains("KG3") } ?: false
            else -> true
        }
        if (!matchesCurrent) {
            val candidate = when (grade) {
                "FIRST_GRADE" -> all.firstOrNull { it.gradeLevel.contains("الأول") || it.gradeLevel.contains("First") }
                "THIRD_LEVEL" -> all.firstOrNull { it.gradeLevel.contains("الثالثة") || it.gradeLevel.contains("Third") || it.gradeLevel.contains("KG3") }
                else -> all.firstOrNull()
            }
            if (candidate != null) {
                _selectedStudentId.value = candidate.id
            }
        }
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
    }

    // Teacher & Parent Actions
    fun saveOrUpdateEvaluation(evaluation: StudentEvaluationEntity) {
        viewModelScope.launch {
            if (evaluation.id == 0L) {
                repository.saveEvaluation(evaluation)
            } else {
                repository.updateEvaluation(evaluation)
            }
        }
    }

    fun acknowledgeEvaluation(studentId: Long, reply: String?) {
        viewModelScope.launch {
            repository.acknowledgeEvaluation(studentId, reply)
        }
    }

    fun recordBehavior(
        studentId: Long,
        points: Int,
        category: String,
        titleAr: String,
        titleEn: String,
        note: String,
        teacher: String
    ) {
        viewModelScope.launch {
            repository.addBehaviorRecord(
                BehaviorRecordEntity(
                    studentId = studentId,
                    pointsDelta = points,
                    category = category,
                    titleArabic = titleAr,
                    titleEnglish = titleEn,
                    note = note,
                    teacherName = teacher
                )
            )
        }
    }

    fun recordProgress(
        studentId: Long,
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
    ) {
        viewModelScope.launch {
            repository.addLearningProgress(
                LearningProgressEntity(
                    studentId = studentId,
                    subjectCode = subjectCode,
                    subjectTitleArabic = subjectAr,
                    subjectTitleEnglish = subjectEn,
                    assessmentType = assessmentType,
                    assessmentTitleArabic = assessmentTitleAr,
                    score = score,
                    maxScore = maxScore,
                    topicArabic = topicAr,
                    topicEnglish = topicEn,
                    teacherNotes = notes
                )
            )
        }
    }

    fun addSideNote(
        studentId: Long,
        teacher: String,
        subject: String,
        category: String,
        text: String,
        isUrgent: Boolean
    ) {
        viewModelScope.launch {
            repository.addSideNote(
                SideNoteEntity(
                    studentId = studentId,
                    teacherName = teacher,
                    subject = subject,
                    category = category,
                    noteText = text,
                    isUrgent = isUrgent,
                    isAcknowledgedByParent = false
                )
            )
        }
    }

    fun acknowledgeSideNote(noteId: Long, response: String?) {
        viewModelScope.launch {
            repository.acknowledgeSideNote(noteId, response)
        }
    }

    fun updateAttendance(studentId: Long, status: String) {
        viewModelScope.launch {
            repository.updateAttendance(studentId, status)
        }
    }

    fun addNewStudent(
        fullNameAr: String,
        fullNameEn: String,
        grade: String,
        guardian: String,
        phone: String
    ) {
        viewModelScope.launch {
            val codeNum = (100..999).random()
            val code = "MH-2026-$codeNum"
            val colors = listOf("#1E3A8A", "#0F766E", "#B45309", "#4338CA", "#047857", "#BE123C")
            val chosenColor = colors.random()

            val newId = repository.addStudent(
                StudentEntity(
                    studentCode = code,
                    fullNameArabic = fullNameAr,
                    fullNameEnglish = fullNameEn,
                    gradeLevel = grade,
                    guardianName = guardian,
                    guardianPhone = phone,
                    avatarColorHex = chosenColor
                )
            )
            // Also create a default Manhal Al-Hikmah evaluation record for the student
            repository.saveEvaluation(
                StudentEvaluationEntity(
                    studentId = newId,
                    subject = "اللغة العربية",
                    gradeLevel = grade,
                    teacherName = "أ. غادة الرفاعي"
                )
            )
            _selectedStudentId.value = newId
        }
    }
}
