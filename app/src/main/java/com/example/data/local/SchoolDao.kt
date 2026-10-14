package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BehaviorRecordEntity
import com.example.data.model.LearningProgressEntity
import com.example.data.model.SideNoteEntity
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {

    // Students
    @Query("SELECT * FROM students ORDER BY id ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
    fun getStudentById(studentId: Long): Flow<StudentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>): List<Long>

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Query("UPDATE students SET attendanceStatus = :status WHERE id = :studentId")
    suspend fun updateAttendance(studentId: Long, status: String)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    // Manhal Al-Hikmah Official Evaluation Sheet Queries
    @Query("SELECT * FROM student_evaluations WHERE studentId = :studentId LIMIT 1")
    fun getEvaluationForStudent(studentId: Long): Flow<StudentEvaluationEntity?>

    @Query("SELECT * FROM student_evaluations")
    fun getAllEvaluations(): Flow<List<StudentEvaluationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvaluation(evaluation: StudentEvaluationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvaluations(evaluations: List<StudentEvaluationEntity>)

    @Update
    suspend fun updateEvaluation(evaluation: StudentEvaluationEntity)

    @Query("UPDATE student_evaluations SET isAcknowledgedByParent = 1, parentReply = :reply, updatedAt = :time WHERE studentId = :studentId")
    suspend fun acknowledgeEvaluation(studentId: Long, reply: String?, time: Long = System.currentTimeMillis())

    // Behavior Records
    @Query("SELECT * FROM behavior_records WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getBehaviorRecordsForStudent(studentId: Long): Flow<List<BehaviorRecordEntity>>

    @Query("SELECT * FROM behavior_records ORDER BY timestamp DESC")
    fun getAllBehaviorRecords(): Flow<List<BehaviorRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorRecord(record: BehaviorRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorRecords(records: List<BehaviorRecordEntity>)

    @Delete
    suspend fun deleteBehaviorRecord(record: BehaviorRecordEntity)

    // Learning Progress
    @Query("SELECT * FROM learning_progress WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getLearningProgressForStudent(studentId: Long): Flow<List<LearningProgressEntity>>

    @Query("SELECT * FROM learning_progress ORDER BY timestamp DESC")
    fun getAllLearningProgress(): Flow<List<LearningProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningProgress(progress: LearningProgressEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningProgressList(list: List<LearningProgressEntity>)

    @Delete
    suspend fun deleteLearningProgress(progress: LearningProgressEntity)

    // Side Notes
    @Query("SELECT * FROM side_notes WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getSideNotesForStudent(studentId: Long): Flow<List<SideNoteEntity>>

    @Query("SELECT * FROM side_notes ORDER BY timestamp DESC")
    fun getAllSideNotes(): Flow<List<SideNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSideNote(note: SideNoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSideNotes(notes: List<SideNoteEntity>)

    @Query("UPDATE side_notes SET isAcknowledgedByParent = 1, parentResponse = :response, acknowledgedAt = :ackTime WHERE id = :noteId")
    suspend fun acknowledgeSideNote(noteId: Long, response: String?, ackTime: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteSideNote(note: SideNoteEntity)

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getStudentCount(): Int

    @Query("SELECT COUNT(*) FROM students WHERE gradeLevel LIKE '%الأول%' OR gradeLevel LIKE '%First%'")
    suspend fun getFirstGradeCount(): Int

    @Query("SELECT COUNT(*) FROM students WHERE gradeLevel LIKE '%الثالثة%' OR gradeLevel LIKE '%Third%'")
    suspend fun getThirdLevelCount(): Int

    @Query("SELECT DISTINCT gradeLevel FROM students ORDER BY gradeLevel ASC")
    fun getAllGradeLevels(): Flow<List<String>>
}
