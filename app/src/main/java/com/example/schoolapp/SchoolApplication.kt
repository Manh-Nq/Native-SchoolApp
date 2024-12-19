package com.example.schoolapp

import android.app.Application
import android.content.Context
import com.example.schoolapp.database.SchoolDataBase

class SchoolApplication : Application() {
    lateinit var schoolDatabase: SchoolDataBase

    companion object {
        private var _application: SchoolApplication? = null
        val application: SchoolApplication get() = _application!!
    }

    override fun onCreate() {
        super.onCreate()
        _application = this
        schoolDatabase = SchoolDataBase.getInstance(application)
    }
}