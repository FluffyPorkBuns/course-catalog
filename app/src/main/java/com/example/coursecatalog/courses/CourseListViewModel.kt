package com.example.coursecatalog.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.database.CourseEntity
import com.example.coursecatalog.database.TermEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CourseListViewModel(
    private val termKey: Long = 0L,
    dataSource: CatalogDatabaseDao) : ViewModel() {

    // reference to catalog database dao
    val database = dataSource

    // reference allows access to close coroutines
    private val viewModelJob = Job()

    // run coroutines on ui scope
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // live data representing term object we're looking at
    private val term = MediatorLiveData<TermEntity>()

    // get courses associated with this term
    val courses = database.getCoursesForTerm(termKey)

    // getter for term object we're looking at
    fun getTerm() = term

    // add database source to term object we're looking at
    init {
        term.addSource(database.getTermWithId(termKey), term::setValue)
    }

    // allows fragment to observe whether to navigate to the term list
    private val _navigateToTermDetail = MutableLiveData<Long?>()
    val navigateToTermDetail: LiveData<Long?>
        get() = _navigateToTermDetail

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

    // adds new blank course and navigates to course detail
    fun onAddCourse() {
        uiScope.launch {
            val newCourse = CourseEntity()
            newCourse.termId = termKey
            val courseId = database.insert(newCourse)
            onCourseClicked(courseId)
            onCourseNavigated()
        }
    }

    private val _navigateToCourseDetail = MutableLiveData<Long>()
    val navigateToCourseDetail
        get() = _navigateToCourseDetail

    fun onCourseClicked(courseId: Long) {
        _navigateToCourseDetail.value = courseId
    }

    fun onCourseNavigated() {
        _navigateToCourseDetail.value = null
    }

}