package com.example.data.repository

import com.example.data.local.InitialDataSeeder
import com.example.data.local.SchoolDao
import com.example.data.model.BehaviorRecordEntity
import com.example.data.model.LearningProgressEntity
import com.example.data.model.SideNoteEntity
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SchoolRepository(private val dao: SchoolDao) {

    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()
    val allEvaluations: Flow<List<StudentEvaluationEntity>> = dao.getAllEvaluations()
    val allSideNotes: Flow<List<SideNoteEntity>> = dao.getAllSideNotes()
    val allBehaviorRecords: Flow<List<BehaviorRecordEntity>> = dao.getAllBehaviorRecords()
    val allLearningProgress: Flow<List<LearningProgressEntity>> = dao.getAllLearningProgress()

    suspend fun initialize() = withContext(Dispatchers.IO) {
        InitialDataSeeder.seedDatabaseIfEmpty(dao)
    }

    fun getStudent(id: Long): Flow<StudentEntity?> = dao.getStudentById(id)

    fun getEvaluationForStudent(studentId: Long): Flow<StudentEvaluationEntity?> =
        dao.getEvaluationForStudent(studentId)

    suspend fun saveEvaluation(evaluation: StudentEvaluationEntity) = withContext(Dispatchers.IO) {
        dao.insertEvaluation(evaluation)
    }

    suspend fun updateEvaluation(evaluation: StudentEvaluationEntity) = withContext(Dispatchers.IO) {
        dao.updateEvaluation(evaluation)
    }

    suspend fun acknowledgeEvaluation(studentId: Long, reply: String?) = withContext(Dispatchers.IO) {
        dao.acknowledgeEvaluation(studentId, reply)
    }

    fun getBehaviorRecords(studentId: Long): Flow<List<BehaviorRecordEntity>> =
        dao.getBehaviorRecordsForStudent(studentId)

    fun getLearningProgress(studentId: Long): Flow<List<LearningProgressEntity>> =
        dao.getLearningProgressForStudent(studentId)

    fun getSideNotes(studentId: Long): Flow<List<SideNoteEntity>> =
        dao.getSideNotesForStudent(studentId)

    suspend fun addStudent(student: StudentEntity): Long = withContext(Dispatchers.IO) {
        dao.insertStudent(student)
    }

    suspend fun updateStudent(student: StudentEntity) = withContext(Dispatchers.IO) {
        dao.updateStudent(student)
    }

    suspend fun updateAttendance(studentId: Long, status: String) = withContext(Dispatchers.IO) {
        dao.updateAttendance(studentId, status)
    }

    suspend fun deleteStudent(student: StudentEntity) = withContext(Dispatchers.IO) {
        dao.deleteStudent(student)
    }

    suspend fun addBehaviorRecord(record: BehaviorRecordEntity): Long = withContext(Dispatchers.IO) {
        dao.insertBehaviorRecord(record)
    }

    suspend fun deleteBehaviorRecord(record: BehaviorRecordEntity) = withContext(Dispatchers.IO) {
        dao.deleteBehaviorRecord(record)
    }

    suspend fun addLearningProgress(progress: LearningProgressEntity): Long = withContext(Dispatchers.IO) {
        dao.insertLearningProgress(progress)
    }

    suspend fun deleteLearningProgress(progress: LearningProgressEntity) = withContext(Dispatchers.IO) {
        dao.deleteLearningProgress(progress)
    }

    suspend fun addSideNote(note: SideNoteEntity): Long = withContext(Dispatchers.IO) {
        dao.insertSideNote(note)
    }

    suspend fun acknowledgeSideNote(noteId: Long, parentResponse: String?) = withContext(Dispatchers.IO) {
        dao.acknowledgeSideNote(noteId, parentResponse)
    }

    suspend fun deleteSideNote(note: SideNoteEntity) = withContext(Dispatchers.IO) {
        dao.deleteSideNote(note)
    }
}
