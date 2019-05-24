package com.example.coursecatalog.terms

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.database.TermEntity
import kotlinx.coroutines.*
import java.util.*
import java.util.logging.Logger

/**
 * AndroidViewModel is identical to ViewModel but it's application aware
 * you must pass the application when instantiating
  */

class TermListViewModel (
    dataSource: CatalogDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val context = application

    // keeps all coroutines under one job
    private var viewModelJob = Job()

    // get ui scope so we can run coroutines on the main thread and have the ui update
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // make local reference to the database dao
    val database = dataSource

    // get list of all terms in database
    val terms = database.getAllTerms()

    // inserts a term object into the database
    private suspend fun insert(term: TermEntity) {
        withContext(Dispatchers.IO) {
            database.insert(term)
        }
    }

    // clears all terms from the database!
    private suspend fun clear(term: TermEntity) {
        withContext(Dispatchers.IO) {
            database.clearTerms()
        }
    }

    // click handler for add term button
    fun onAddTerm() {
        uiScope.launch {
            val testTerm = TermEntity()
            testTerm.termTitle = "test term"

            insert(testTerm)

        }
    }

    /**
     * live data lets app observe and see when a request to navigate
     * to the next fragment
      */

    private val _navigateToTermDetail = MutableLiveData<Long>()
    val navigateToTermDetail
        get() = _navigateToTermDetail

    fun onTermClicked(termId: Long) {
        _navigateToTermDetail.value = termId
    }

    fun onTermNavigated() {
        _navigateToTermDetail.value = null
    }

}