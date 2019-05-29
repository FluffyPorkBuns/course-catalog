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

    // get courses associated with this term
    val assessments = database.getAssessmentsByCourse(courseKey)

    fun getCourse() = course

    // add database source to course object we're looking at
    init {
        course.addSource(database.getCourseWithId(courseKey), course::setValue)
    }

    // allows fragment to observe whether to navigate to the course list
    private val _navigateToTermDetail = MutableLiveData<Long?>()
    val navigateToTermDetail: LiveData<Long?>
        get() = _navigateToTermDetail

    // tells app to cancel all coroutines when closing this fragment
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // tells observer it's time to navigate to the course list
    fun onNavigateToTermDetail() {
        _navigateToTermDetail.value = termKey
    }

    // resets back to null so observer knows not to navigate anymore
    fun onTermDetailNavigated() {
        _navigateToTermDetail.value = null
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

    // adds a new assessment and navigates to assessment detail view
    fun onNewAssessment() {
        uiScope.launch {
            val assessment = Assessment()
            assessment.courseId = courseKey
            val newId = database.insert(assessment)
            onAssessmentClicked(newId)
            onAssessmentNavigated()
        }
    }

    fun getTermId(){
        uiScope.launch {
            termKey = database.getTermIdForCourse(courseKey)
        }
    }

    private val _navigateToAssessmentDetail = MutableLiveData<Long>()
    val navigateToAssessmentDetail
        get() = _navigateToAssessmentDetail

    fun onAssessmentClicked(assessmentId: Long) {
        _navigateToAssessmentDetail.value = assessmentId
    }


    fun onAssessmentNavigated() {
        _navigateToAssessmentDetail.value = null
    }

    fun onDelete() {
        uiScope.launch {
            database.deleteCourseById(courseKey)
            onNavigateToTermDetail()
            onTermDetailNavigated()
        }
    }

}