package com.example.coursecatalog.terms

import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
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
import javax.security.auth.login.LoginException

class TermDetailViewModel(
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
    private val _navigateToTermList = MutableLiveData<Boolean?>()
    val navigateToTermList: LiveData<Boolean?>
        get() = _navigateToTermList

    // allows fragment to observe when to navigate to course picker fragment
    private val _navigateToCoursePicker = MutableLiveData<Long?>()
    val navigateToCoursePicker: LiveData<Long?>
        get() = _navigateToCoursePicker

    // signal to fragment to navigate to course picker fragment
    fun onNavigateToCoursePicker() {
        _navigateToCoursePicker.value = termKey
    }

    // reset status of navigation flag
    fun onCoursePickerNavigated() {
        _navigateToCoursePicker.value = null
    }

    // tells app to cancel all coroutines when closing this fragment
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // tells observer it's time to navigate to the term list
    fun onNavigateToTermList() {
        _navigateToTermList.value = true
    }

    // resets back to null so observer knows not to navigate anymore
    fun onTermListNavigated() {
        _navigateToTermList.value = null
    }

    fun onSaveTerm(title: String, startDate: String, endDate: String) {
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

    // adds new blank course and navigates to course detail
    fun onAddCourse() {
        uiScope.launch {
            val newCourse = CourseEntity()
            val courseId = database.insert(newCourse)
            Log.i("course add debug", "$courseId")
            val termCourseJoin = TermCourseJoin(termKey, courseId)
            database.insert(termCourseJoin)
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