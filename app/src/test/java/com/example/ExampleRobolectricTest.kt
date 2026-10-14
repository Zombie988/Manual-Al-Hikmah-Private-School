package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SchoolDatabase
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var db: SchoolDatabase
    private lateinit var repository: SchoolRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SchoolDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = SchoolRepository(db.schoolDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("منهل الحكمة", appName)
    }

    @Test
    fun testManhalAlHikmahSeededStudentsAndEvaluations() = runBlocking {
        repository.initialize()
        val students = repository.allStudents.first()
        assertTrue("Seeded students should not be empty", students.isNotEmpty())

        val adam = students.find { it.fullNameArabic.contains("آدم البارد") }
        assertTrue("Adam Al-Bared should be among seeded students", adam != null)

        val evals = repository.allEvaluations.first()
        assertTrue("Evaluations should not be empty", evals.isNotEmpty())

        val adamEval = evals.find { it.studentId == adam!!.id }
        assertTrue("Adam evaluation should exist", adamEval != null)
        assertEquals("جيد", adamEval!!.reading)
        assertEquals("متوسط", adamEval.writing)
        assertTrue(adamEval.isPolite)
    }

    @Test
    fun testFirstGradeStudentsAndEvaluationsSeeded() = runBlocking {
        repository.initialize()
        val students = repository.allStudents.first()

        val firstGrade = students.filter { it.gradeLevel.contains("الأول") }
        assertTrue("First grade students should be seeded", firstGrade.isNotEmpty())
        assertEquals(17, firstGrade.size)

        val layanAbuJaib = firstGrade.find { it.fullNameArabic.contains("ليان أبو جيب") }
        assertTrue("Layan Abu Jaib should be in First Grade", layanAbuJaib != null)

        val layanBali = firstGrade.find { it.fullNameArabic.contains("ليان بالي") }
        assertTrue("Layan Bali should be in First Grade", layanBali != null)

        val lilia = firstGrade.find { it.fullNameArabic.contains("ليليا") }
        assertTrue("Lilia Maatouq Qataa should be in First Grade", lilia != null)

        val evals = repository.allEvaluations.first()
        val layanEval = evals.find { it.studentId == layanAbuJaib!!.id }
        assertTrue("Layan Abu Jaib evaluation should exist", layanEval != null)
        assertEquals("جيد", layanEval!!.reading)
        assertEquals("جيد", layanEval.writing)
        assertEquals("أ. ريم الحلبي", layanEval.teacherName)
        assertTrue(layanEval.isPolite)
    }
}
