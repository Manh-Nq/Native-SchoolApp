package com.example.schoolapp.ui

import androidx.lifecycle.LiveData
import com.example.schoolapp.database.dao.SchoolDao
import com.example.schoolapp.database.model.ClassEntity
import com.example.schoolapp.database.model.ClassWithTeachers
import com.example.schoolapp.database.model.StudentEntity
import com.example.schoolapp.database.model.TeacherClassEntity
import com.example.schoolapp.database.model.TeacherEntity
import com.example.schoolapp.database.model.TeacherWithClasses

// --- Repository ---
class SchoolRepository(private val dao: SchoolDao) {
    val teachersWithClasses: LiveData<List<TeacherWithClasses>> = dao.getTeachersWithClasses()
    val classesWithTeachers: LiveData<List<ClassWithTeachers>> = dao.getClassesWithTeachers()
    val studentData: LiveData<List<StudentEntity>> = dao.getAllStudents()

    suspend fun addClass(classEntity: ClassEntity) = dao.insertClass(classEntity)
    suspend fun addStudent(studentEntity: StudentEntity) = dao.insertStudent(studentEntity)
    suspend fun addTeacher(teacherEntity: TeacherEntity) = dao.insertTeacher(teacherEntity)
    suspend fun addTeacherClass(teacherClassEntity: TeacherClassEntity) =
        dao.insertTeacherClass(teacherClassEntity)

    suspend fun assignTeacherToClass(teacherId: Int, classId: Int) = dao.insertTeacherClass(
        TeacherClassEntity(teacherId = teacherId, classId = classId)
    )

    suspend fun updateClass(classEntity: ClassEntity) = dao.updateClass(classEntity)
    suspend fun updateStudent(studentEntity: StudentEntity) = dao.updateStudent(studentEntity)
    suspend fun updateTeacher(teacherEntity: TeacherEntity) = dao.updateTeacher(teacherEntity)

    suspend fun deleteClass(classEntity: ClassEntity) = dao.deleteClass(classEntity)
    suspend fun deleteStudent(studentEntity: StudentEntity) = dao.deleteStudent(studentEntity)
    suspend fun deleteTeacher(teacherEntity: TeacherEntity) = dao.deleteTeacher(teacherEntity)
    suspend fun deleteTeacherClass(teacherClassEntity: TeacherClassEntity) =
        dao.deleteTeacherClass(teacherClassEntity)

    suspend fun getClassById(classId: Int): ClassEntity? = dao.getClassById(classId)
    suspend fun getStudentById(studentId: Int): StudentEntity? = dao.getStudentById(studentId)
    suspend fun getTeacherById(teacherId: Int): TeacherEntity? = dao.getTeacherById(teacherId)

    fun getTeacherWithClassesById(teacherId: Int): LiveData<TeacherWithClasses> =
        dao.getTeacherWithClassesById(teacherId)

    fun getClassWithTeachers(classId: Int): LiveData<ClassWithTeachers> =
        dao.getClassWithTeachersById(classId)

    fun getAllStudent(): LiveData<List<StudentEntity>> = dao.getAllStudents()
}