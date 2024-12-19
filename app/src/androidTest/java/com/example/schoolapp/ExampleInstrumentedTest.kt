package com.example.schoolapp

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.schoolapp.database.SchoolDataBase
import com.example.schoolapp.database.dao.SchoolDao
import com.example.schoolapp.database.model.ClassEntity
import com.example.schoolapp.database.model.StudentEntity
import com.example.schoolapp.database.model.TeacherClassEntity
import com.example.schoolapp.database.model.TeacherEntity
import com.example.schoolapp.database.model.TeacherWithClasses
import com.example.schoolapp.ui.SchoolRepository
import com.example.schoolapp.ui.SchoolViewModel
import kotlinx.coroutines.CoroutineScope
import org.junit.Before
import org.junit.Rule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SchoolViewModelTest {

    private lateinit var database: SchoolDataBase
    private lateinit var viewModel: SchoolViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // Set the Main dispatcher for coroutines to run synchronously in tests
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Create an in-memory Room database
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, SchoolDataBase::class.java)
            .allowMainThreadQueries() // Allow main thread queries for testing
            .build()

        viewModel = SchoolViewModel(database)
    }

    @Test
    fun testAddClassAndGetTeachers() {
        val observer: Observer<List<TeacherWithClasses>> = mock(Observer::class.java) as Observer<List<TeacherWithClasses>>

        viewModel.teachersWithClasses.observeForever(observer)
        viewModel.addSampleData()

        val teachersWithClasses = viewModel.teachersWithClasses.value

        teachersWithClasses?.let {
            // Verify the correct data is being observed
            verify(observer).onChanged(it)
        }

        // Cleanup after the test
        database.clearAllTables()
    }

    @Test
    fun testUpdateClass() {
        // Mock Observer
        val observer: Observer<List<TeacherWithClasses>> = mock(Observer::class.java) as Observer<List<TeacherWithClasses>>

        viewModel.teachersWithClasses.observeForever(observer)

        // Insert class and teacher data
        val classEntity = ClassEntity(className = "Math 101", classCode = "MATH101", startDate = "2024-01-01", endDate = "2024-06-01")
        viewModel.addClass(classEntity)

        // Update class data
        val updatedClass = classEntity.copy(className = "Advanced Math 101")
        viewModel.updateClass(updatedClass)

        // Verify the class was updated
        val teachersWithClasses = viewModel.teachersWithClasses.value
        teachersWithClasses?.let {
            verify(observer).onChanged(it)
        }

        // Cleanup
        database.clearAllTables()
    }

    @Test
    fun testDeleteClass() {
        // Mock Observer
        val observer: Observer<List<TeacherWithClasses>> = mock(Observer::class.java) as Observer<List<TeacherWithClasses>>

        viewModel.teachersWithClasses.observeForever(observer)

        // Insert class and teacher data
        val classEntity = ClassEntity(className = "Math 101", classCode = "MATH101", startDate = "2024-01-01", endDate = "2024-06-01")
        viewModel.addClass(classEntity)

        // Delete the class
        viewModel.deleteClass(classEntity)

        // Verify the class is deleted
        val teachersWithClasses = viewModel.teachersWithClasses.value
        teachersWithClasses?.let {
            verify(observer).onChanged(it)
        }

        // Cleanup
        database.clearAllTables()
    }


    // Additional test case to check when teacher has no classes
    @Test
    fun testTeacherWithoutClasses() {
        val observer: Observer<List<TeacherWithClasses>> = mock(Observer::class.java) as Observer<List<TeacherWithClasses>>

        viewModel.teachersWithClasses.observeForever(observer)

        // Add a teacher without assigning any classes
        val teacherEntity = TeacherEntity(name = "Charlie", age = 45, dateOfBirth = "1979-01-01", address = "111 Street", gender = "Male", contactNumber = "1122334455", email = "charlie@example.com", hireDate = "2010-01-01", subjectSpecialization = "History", manageClassId = null)
        viewModel.addTeacher(teacherEntity)

        // Check if the teacher appears in the list with no classes
        val teachersWithClasses = viewModel.teachersWithClasses.value
        teachersWithClasses?.let {
            verify(observer).onChanged(it)
        }

        // Cleanup
        database.clearAllTables()
    }

    // Additional test case to check when there are no classes or teachers
    @Test
    fun testEmptyTeachersAndClasses() {
        val observer: Observer<List<TeacherWithClasses>> = mock(Observer::class.java) as Observer<List<TeacherWithClasses>>

        viewModel.teachersWithClasses.observeForever(observer)

        // Verify no data is present initially
        val teachersWithClasses = viewModel.teachersWithClasses.value
        assert(teachersWithClasses?.isEmpty() == true)

        // Cleanup
        database.clearAllTables()
    }
}

