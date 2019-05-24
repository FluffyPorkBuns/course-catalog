package com.example.coursecatalog.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.database.TermEntity
import kotlinx.coroutines.Job

class TermDetailViewModel(
    private val termKey: Long = 0L,
    dataSource: CatalogDatabaseDao) : ViewModel() {

    // reference to catalog database dao
    val database = dataSource

    // reference allows access to close coroutines
    private val viewModelJob = Job()

    // live data representing term object we're looking at
    private val term = MediatorLiveData<TermEntity>()

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

}