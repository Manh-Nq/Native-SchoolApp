package com.example.schoolapp.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

// --- Entities ---
@Entity
data class ClassEntity(
    @PrimaryKey(autoGenerate = true) val classId: Int = 0,
    val className: String,
    val classCode: String,
    val startDate: String,
    val endDate: String
)

@Entity
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val studentId: Int = 0,
    val name: String,
    val age: Int,
    val dateOfBirth: String,
    val address: String,
    val gender: String?,
    val contactNumber: String?,
    val email: String?,
    val enrollmentDate: String,
    val gpa: Double,
    val classId: Int?
)

@Entity(primaryKeys = ["teacherId", "classId"])
data class TeacherClassEntity(
    val teacherId: Int,
    val classId: Int
)

@Entity
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true) val teacherId: Int = 0,
    val name: String,
    val age: Int,
    val dateOfBirth: String,
    val address: String,
    val gender: String?,
    val contactNumber: String?,
    val email: String?,
    val hireDate: String,
    val subjectSpecialization: String,
    val manageClassId: Int?
)

// --- Relation Models ---
data class TeacherWithClasses(
    @Embedded val teacher: TeacherEntity,
    @Relation(
        parentColumn = "teacherId",
        entityColumn = "classId",
        associateBy = Junction(TeacherClassEntity::class)
    )
    val teachingClasses: List<ClassEntity>
)

data class ClassWithTeachers(
    @Embedded val singleClass: ClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "teacherId",
        associateBy = Junction(TeacherClassEntity::class)
    )
    val teachers: List<TeacherEntity>
)