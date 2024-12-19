package com.example.schoolapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.schoolapp.database.model.ClassEntity
import com.example.schoolapp.database.model.ClassWithTeachers
import com.example.schoolapp.database.model.StudentEntity
import com.example.schoolapp.database.model.TeacherClassEntity
import com.example.schoolapp.database.model.TeacherEntity
import com.example.schoolapp.database.model.TeacherWithClasses

@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(studentEntity: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacherEntity: TeacherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherClass(teacherClassEntity: TeacherClassEntity)

    @Update
    suspend fun updateClass(classEntity: ClassEntity)

    @Update
    suspend fun updateStudent(studentEntity: StudentEntity)

    @Update
    suspend fun updateTeacher(teacherEntity: TeacherEntity)

    @Delete
    suspend fun deleteClass(classEntity: ClassEntity)

    @Delete
    suspend fun deleteStudent(studentEntity: StudentEntity)

    @Delete
    suspend fun deleteTeacher(teacherEntity: TeacherEntity)

    @Delete
    suspend fun deleteTeacherClass(teacherClassEntity: TeacherClassEntity)

    @Transaction
    @Query("SELECT * FROM StudentEntity")
    fun getAllStudents(): LiveData<List<StudentEntity>>

    @Transaction
    @Query("SELECT * FROM TeacherEntity")
    fun getTeachersWithClasses(): LiveData<List<TeacherWithClasses>>

    @Transaction
    @Query("SELECT * FROM ClassEntity")
    fun getClassesWithTeachers(): LiveData<List<ClassWithTeachers>>

    @Transaction
    @Query("SELECT * FROM TeacherEntity WHERE teacherId = :teacherId")
    fun getTeacherWithClassesById(teacherId: Int): LiveData<TeacherWithClasses>

    @Transaction
    @Query("SELECT * FROM ClassEntity WHERE classId = :classId")
    fun getClassWithTeachersById(classId: Int): LiveData<ClassWithTeachers>

    @Query("SELECT * FROM ClassEntity WHERE classId = :classId")
    suspend fun getClassById(classId: Int): ClassEntity?

    @Query("SELECT * FROM StudentEntity WHERE studentId = :studentId")
    suspend fun getStudentById(studentId: Int): StudentEntity?

    @Query("SELECT * FROM TeacherEntity WHERE teacherId = :teacherId")
    suspend fun getTeacherById(teacherId: Int): TeacherEntity?
}