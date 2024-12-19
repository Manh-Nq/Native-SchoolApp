package com.example.schoolapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.schoolapp.databinding.ActivityMainBinding
import com.example.schoolapp.ui.SchoolViewModel
import com.example.schoolapp.ui.SchoolViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: SchoolViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val schoolViewModelFactory =
            SchoolViewModelFactory(SchoolApplication.application.schoolDatabase)

        // Use the factory to create the ViewModel
        viewModel =
            ViewModelProvider(this, schoolViewModelFactory)[SchoolViewModel::class.java]


//        viewModel.populateTestData()

        initViews()
        observerData()
    }

    private fun observerData() {
        // Observe and print data
        viewModel.teachersWithClasses.observe(this) { teachersWithClasses ->
            teachersWithClasses.forEach {
                Log.d("ManhNQ","Teacher: ${it.teacher.name}")
                it.teachingClasses.forEach { teachingClass ->
                    Log.d("ManhNQ","- Class: ${teachingClass.className}")
                }
            }
        }

        viewModel.classesWithTeachers.observe(this) { classesWithTeachers ->
            classesWithTeachers.forEach {
                Log.d("ManhNQ","Class: ${it.singleClass.className}")
                it.teachers.forEach { teacher ->
                    Log.d("ManhNQ","- Teacher: ${teacher.name}")
                }
            }
        }
    }

    private fun initViews() {
        binding.init.setOnClickListener {
        }
        binding.addDataBtn.setOnClickListener {
            viewModel.addSampleData()
        }
    }
}