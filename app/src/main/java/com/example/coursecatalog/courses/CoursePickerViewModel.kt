package com.example.coursecatalog.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.database.CourseEntity
import com.example.coursecatalog.database.TermCourseJoin
import com.example.coursecatalog.database.TermEntity
import com.example.coursecatalog.util.formatStringAsDate
import kotlinx.coroutines.*

class CoursePickerViewModel(
    private val termKey: Long = 0L,
    dataSource: CatalogDatabaseDao
) : ViewModel() {

    // reference to catalog database dao
    val database = dataSource

    // reference allows access to close coroutines
    private val viewModelJob = Job()

    // run coroutines on ui scope
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // live data representing term object we're looking at
    private val term = MediatorLiveData<TermEntity>()

    // get all courses
    val courses = database.getAllCourses()

    // getter for term object we're looking at
    fun getTerm() = term

    // add database source to term object we're looking at
    init {
        term.addSource(database.getTermWithId(termKey), term::setValue)
    }

    // allows fragment to observe whether to navigate to course detail
    private val _navigateToTermDetail = MutableLiveData<Long?>()
    val navigateToTermDetail: LiveData<Long?>
        get() = _navigateToTermDetail

    // allows fragment to observe whether to navigate to the course detail
    private val _navigateToCourseDetail = MutableLiveData<Long?>()
    val navigateToCourseDetail: LiveData<Long?>
        get() = _navigateToCourseDetail

    // tells app to cancel all coroutines when closing this fragment
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // tells observer it's time to navigate to the term list
    fun onNavigateToTermDetail() {
        _navigateToTermDetail.value = termKey
    }

    // resets back to null so observer knows not to navigate anymore
    fun onTermDetailNavigated() {
        _navigateToTermDetail.value = null
    }

    fun onAddCourseToTerm(title: String, startDate: String, endDate: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val updatedTerm = database.getTerm(termKey) ?: return@withContext
                updatedTerm.termTitle = title
                updatedTerm.startDate = formatStringAsDate(startDate)
                updatedTerm.endDate = formatStringAsDate(endDate)
                database.update(updatedTerm)
            }
        }
    }

    // click handler for add term button
    fun onAddNewCourse() {
        uiScope.launch {
            val testCourse = CourseEntity()

            testCourse.courseTitle = "Test Course"

            insert(testCourse)

        }
    }

    // inserts a course object into the database
    private suspend fun insert(course: CourseEntity) {
        withContext(Dispatchers.IO) {
            val courseId = database.insert(course)
            val termCourseEntity = TermCourseJoin(termKey, courseId,
                "want to take", "sample note")
            database.insert(termCourseEntity)
        }
    }

    fun onCourseClicked(courseId: Long) {
        _navigateToCourseDetail.value = courseId
    }


    fun onCourseNavigated() {
        _navigateToCourseDetail.value = null
    }

}