package com.example.schoolapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.schoolapp.database.dao.SchoolDao
import com.example.schoolapp.database.model.ClassEntity
import com.example.schoolapp.database.model.StudentEntity
import com.example.schoolapp.database.model.TeacherClassEntity
import com.example.schoolapp.database.model.TeacherEntity

// --- Database ---
@Database(
    entities = [ClassEntity::class, StudentEntity::class, TeacherEntity::class, TeacherClassEntity::class],
    version = 1
)
abstract class SchoolDataBase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile private var INSTANCE: SchoolDataBase? = null

        fun getInstance(context: android.content.Context): SchoolDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDataBase::class.java,
                    "school_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
