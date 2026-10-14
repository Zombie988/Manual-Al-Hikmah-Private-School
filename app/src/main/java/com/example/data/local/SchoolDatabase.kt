package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BehaviorRecordEntity
import com.example.data.model.LearningProgressEntity
import com.example.data.model.SideNoteEntity
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity

@Database(
    entities = [
        StudentEntity::class,
        StudentEvaluationEntity::class,
        BehaviorRecordEntity::class,
        LearningProgressEntity::class,
        SideNoteEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getDatabase(context: Context): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "manhal_alhikmah_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
