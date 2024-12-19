package com.example.schoolapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schoolapp.database.SchoolDataBase
import com.example.schoolapp.database.model.ClassEntity
import com.example.schoolapp.database.model.StudentEntity
import com.example.schoolapp.database.model.TeacherClassEntity
import com.example.schoolapp.database.model.TeacherEntity
import com.example.schoolapp.database.model.TeacherWithClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.random.Random

class SchoolViewModelFactory(val db: SchoolDataBase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolViewModel::class.java)) {
            return SchoolViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SchoolViewModel(private val db: SchoolDataBase) : ViewModel() {
    private val repository: SchoolRepository by lazy {
        SchoolRepository(db.schoolDao())
    }
    val teachersWithClasses = repository.teachersWithClasses
    val classesWithTeachers = repository.classesWithTeachers
    val studentData = repository.getAllStudent()

    fun addSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClass(ClassEntity(1, "Math 101", "MATH101", "2024-01-01", "2024-06-01"))
            repository.addClass(ClassEntity(3, "Math 103", "MATH103", "2024-01-01", "2024-06-01"))
            repository.addClass(ClassEntity(4, "Math 104", "MATH104", "2024-01-01", "2024-06-01"))
            repository.addClass(
                ClassEntity(
                    2,
                    "Physics 101",
                    "PHYS101",
                    "2024-01-01",
                    "2024-06-01"
                )
            )

            repository.addTeacher(
                TeacherEntity(
                    1,
                    "Alice",
                    35,
                    "1989-01-01",
                    "123 Street",
                    "Female",
                    "1234567890",
                    "alice@example.com",
                    "2015-09-01",
                    "Mathematics",
                    1
                )
            )
            repository.addTeacher(
                TeacherEntity(
                    2,
                    "Bob",
                    40,
                    "1984-01-01",
                    "456 Street",
                    "Male",
                    "9876543210",
                    "bob@example.com",
                    "2010-09-01",
                    "Physics",
                    2
                )
            )

            repository.addTeacherClass(TeacherClassEntity(1, 1))
            repository.addTeacherClass(TeacherClassEntity(1, 4))
            repository.addTeacherClass(TeacherClassEntity(2, 4))

            repository.addTeacherClass(TeacherClassEntity(2, 2))
            repository.addTeacherClass(TeacherClassEntity(2, 3))
        }
    }

    fun addClass(classEntity: ClassEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClass(classEntity)
        }
    }

    fun updateClass(classEntity: ClassEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateClass(classEntity)
        }
    }

    fun deleteClass(classEntity: ClassEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteClass(classEntity)
        }
    }

    fun getClassById(classId: Int, callback: (ClassEntity?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val classEntity = repository.getClassById(classId)
            callback(classEntity)
        }
    }

    fun getTeacherById(teacherId: Int, callback: (TeacherEntity?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val teacherEntity = repository.getTeacherById(teacherId)
            callback(teacherEntity)
        }
    }

    fun getTeacherWithClassesById(teacherId: Int, callback: (TeacherWithClasses?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val teacherWithClasses = repository.getTeacherWithClassesById(teacherId)
            teacherWithClasses.observeForever {
                callback(it)
            }
        }
    }

    fun addTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            repository.addTeacher(teacher)
        }
    }

    fun addStudent(student: StudentEntity) {
        viewModelScope.launch {
            repository.addStudent(student)
        }
    }

    suspend fun getStudentById(studentId: Int): StudentEntity? {
        return repository.getStudentById(studentId)
    }

    fun getAllStudents(): LiveData<List<StudentEntity>> {
        return repository.getAllStudent()
    }

    fun assignTeacherToClass(teacherId: Int, classId: Int) {
        viewModelScope.launch {
            repository.assignTeacherToClass(teacherId, classId)
        }
    }
}
