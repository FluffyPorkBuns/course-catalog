package com.example.coursecatalog.courses

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coursecatalog.database.*
import com.example.coursecatalog.util.formatStringAsDate
import kotlinx.coroutines.*

class CourseDetailViewModel(
    private val courseKey: Long = 0L,
    dataSource: CatalogDatabaseDao) : ViewModel() {

    // reference to catalog database dao
    val database = dataSource

    // reference allows access to close coroutines
    private val viewModelJob = Job()

    private var termKey = 0L

    // run coroutines on ui scope
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // live data representing course object we're looking at
    private val course = MediatorLiveData<CourseEntity>()

    fun getCourse() = course

    // add database source to course object we're looking at
    init {
        course.addSource(database.getCourseWithId(courseKey), course::setValue)
    }

    // allows fragment to observe whether to navigate to the course list
    private val _navigateToCourseList = MutableLiveData<Long?>()
    val navigateToCourseList: LiveData<Long?>
        get() = _navigateToCourseList

    // tells observer it's time to navigate to the course list
    fun onNavigateToCourseList() {
        _navigateToCourseList.value = termKey
    }

    // resets back to null so observer knows not to navigate anymore
    fun onCourseListNavigated() {
        _navigateToCourseList.value = null
    }

    // tells app to cancel all coroutines when closing this fragment
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onSaveCourse(title: String,
                     status: String,
                     start: String,
                     end: String,
                     mentorName: String,
                     mentorPhone: String,
                     mentorEmail: String,
                     notes: String

    ) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val updatedCourse = database.getCourse(courseKey) ?: return@withContext
                Log.i("courseSaveBug","original course: $updatedCourse")
                updatedCourse.courseTitle = title
                updatedCourse.status = status
                updatedCourse.startDate = formatStringAsDate(start)
                updatedCourse.endDate = formatStringAsDate(end)
                updatedCourse.mentor.name = mentorName
                updatedCourse.mentor.email = mentorEmail
                updatedCourse.mentor.phone = mentorPhone
                updatedCourse.notes = notes
                Log.i("courseSaveBug","course being saved: $updatedCourse")

                database.update(updatedCourse)
            }
        }
    }

    fun getTermId(){
        uiScope.launch {
            termKey = database.getTermIdForCourse(courseKey)
        }
    }

    private val _navigateToAssessmentList = MutableLiveData<Long>()
    val navigateToAssessmentList
        get() = _navigateToAssessmentList

    fun onNavigateToAssessmentList() {
        _navigateToAssessmentList.value = courseKey
    }


    fun onAssessmentListNavigated() {
        _navigateToAssessmentList.value = null
    }

    fun onDelete() {
        uiScope.launch {
            database.deleteCourseById(courseKey)
            onNavigateToCourseList()
            onCourseListNavigated()
        }
    }

}