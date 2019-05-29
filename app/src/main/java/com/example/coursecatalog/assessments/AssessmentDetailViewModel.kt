package com.example.coursecatalog.assessments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coursecatalog.database.Assessment
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.util.formatStringAsDate
import kotlinx.coroutines.*

class AssessmentDetailViewModel(
    private val assessmentKey: Long = 0L,
    dataSource: CatalogDatabaseDao
) : ViewModel() {

    // reference to catalog database dao
    val database = dataSource

    // reference allows access to close coroutines
    private val viewModelJob = Job()

    private var courseKey = 0L

    // run coroutines on ui scope
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // live data representing assessment object we're looking at
    private val assessment = MediatorLiveData<Assessment>()

    fun getAssessment() = assessment

    // add database source to assessment object we're looking at
    init {
        assessment.addSource(database.getAssessmentById(assessmentKey), assessment::setValue)
    }

    // allows fragment to observe whether to navigate to the assessment list
    private val _navigateToCourseDetail = MutableLiveData<Long?>()
    val navigateToCourseDetail: LiveData<Long?>
        get() = _navigateToCourseDetail

    // tells app to cancel all coroutines when closing this fragment
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // tells observer it's time to navigate to the assessment list
    fun onNavigateToCourseDetail() {
        _navigateToCourseDetail.value = courseKey
    }

    // resets back to null so observer knows not to navigate anymore
    fun onCourseDetailNavigated() {
        _navigateToCourseDetail.value = null
    }

    fun onSaveAssessment(title: String,
                     type: String,
                     dueDate: String,
                     notes: String

    ) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val updatedAssessment = database.getAssessment(assessmentKey) ?: return@withContext
                updatedAssessment.title = title
                updatedAssessment.type = type
                updatedAssessment.dueDate = formatStringAsDate(dueDate)
                updatedAssessment.notes = notes

                database.update(updatedAssessment)
            }
        }
    }

    fun onDelete() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteAssessmentById(assessmentKey)
            }
        }
    }

    // fetch the courseId of the course associated with this assessment
    fun getCourseId(){
        uiScope.launch {
            courseKey = database.getCourseIdByAssessmentId(assessmentKey)
        }
    }

}