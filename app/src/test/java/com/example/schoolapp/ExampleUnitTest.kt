package com.example.schoolapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.manytomany.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockRepository: SchoolRepository

    @Mock
    lateinit var mockObserver: Observer<List<TeacherWithClasses>>

    private lateinit var viewModel: SchoolViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SchoolViewModel(mockRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test addSampleData() inserts data correctly`() = runTest {
        // Arrange
        val classEntity = ClassEntity(1, "Math 101", "MATH101", "2024-01-01", "2024-06-01")
        val teacherEntity = TeacherEntity(1, "Alice", 35, "1989-01-01", "123 Street", "Female", "1234567890", "alice@example.com", "2015-09-01", "Mathematics", null)
        val teacherClassEntity = TeacherClassEntity(1, 1) // Alice teaches Math 101

        `when`(mockRepository.addClass(classEntity)).thenReturn(Unit)
        `when`(mockRepository.addTeacher(teacherEntity)).thenReturn(Unit)
        `when`(mockRepository.addTeacherClass(teacherClassEntity)).thenReturn(Unit)

        // Act
        viewModel.addSampleData()

        // Assert
        verify(mockRepository).addClass(classEntity)
        verify(mockRepository).addTeacher(teacherEntity)
        verify(mockRepository).addTeacherClass(teacherClassEntity)
    }

    @Test
    fun `test getTeachersWithClasses() returns expected data`() = runTest {
        // Arrange
        val teacherWithClasses = TeacherWithClasses(
            teacher = TeacherEntity(1, "Alice", 35, "1989-01-01", "123 Street", "Female", "1234567890", "alice@example.com", "2015-09-01", "Mathematics", null),
            teachingClasses = listOf(ClassEntity(1, "Math 101", "MATH101", "2024-01-01", "2024-06-01"))
        )
        `when`(mockRepository.teachersWithClasses).thenReturn(mockLiveData(teacherWithClasses))

        // Act
        viewModel.teachersWithClasses.observeForever(mockObserver)

        // Assert
        verify(mockObserver).onChanged(listOf(teacherWithClasses))
    }

    @Test
    fun `test getClassById returns correct data`() = runTest {
        // Arrange
        val classEntity = ClassEntity(1, "Math 101", "MATH101", "2024-01-01", "2024-06-01")
        `when`(mockRepository.getClassById(1)).thenReturn(classEntity)

        // Act
        var resultClass: ClassEntity? = null
        viewModel.getClassById(1) {
            resultClass = it
        }

        // Assert
        assertEquals(classEntity, resultClass)
    }

    @Test
    fun `test getTeacherWithClassesById returns expected data`() = runTest {
        // Arrange
        val teacherWithClasses = TeacherWithClasses(
            teacher = TeacherEntity(1, "Alice", 35, "1989-01-01", "123 Street", "Female", "1234567890", "alice@example.com", "2015-09-01", "Mathematics", null),
            teachingClasses = listOf(ClassEntity(1, "Math 101", "MATH101", "2024-01-01", "2024-06-01"))
        )
        `when`(mockRepository.getTeacherWithClassesById(1)).thenReturn(mockLiveData(teacherWithClasses))

        // Act
        viewModel.getTeacherWithClassesById(1) { result ->
            // Assert
            assertEquals(teacherWithClasses, result)
        }
    }

    @Test
    fun `test getTeacherWithClassesById returns null when no teacher found`() = runTest {
        // Arrange
        `when`(mockRepository.getTeacherWithClassesById(99)).thenReturn(mockLiveData(null))

        // Act
        var result: TeacherWithClasses? = null
        viewModel.getTeacherWithClassesById(99) { teacherWithClasses ->
            result = teacherWithClasses
        }

        // Assert
        assertEquals(null, result)
    }

    // Helper function to mock LiveData
    private fun <T> mockLiveData(data: T): LiveData<T> {
        val liveData = mock(LiveData::class.java) as LiveData<T>
        `when`(liveData.value).thenReturn(data)
        return liveData
    }
}
