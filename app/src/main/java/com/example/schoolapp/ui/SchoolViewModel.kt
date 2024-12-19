package com.example.schoolapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schoolapp.database.SchoolDataBase
import com.example.schoolapp.database.model.ClassEntity
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

    fun addSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClass(ClassEntity(1, "Math 101", "MATH101", "2024-01-01", "2024-06-01"))
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
                    null
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
                    null
                )
            )

            repository.addTeacherClass(TeacherClassEntity(1, 1)) // Alice teaches Math 101
            repository.addTeacherClass(TeacherClassEntity(2, 2)) // Bob teaches Physics 101
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
}
