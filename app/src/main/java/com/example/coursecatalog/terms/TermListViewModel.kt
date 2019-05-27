package com.example.coursecatalog.terms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.coursecatalog.database.CatalogDatabaseDao
import com.example.coursecatalog.database.TermEntity
import kotlinx.coroutines.*

/**
 * AndroidViewModel is identical to ViewModel but it's application aware
 * you must pass the application when instantiating
  */

class TermListViewModel (
    dataSource: CatalogDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    // getTerm context
    val context = application

    // keeps all coroutines under one job
    private var viewModelJob = Job()

    // getTerm ui scope so we can run coroutines on the main thread and have the ui update
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // make local reference to the database dao
    val database = dataSource

    // getTerm list of all terms in database
    val terms = database.getAllTerms()

    // inserts a term object into the database
    private suspend fun insert(term: TermEntity): Long {
        var newId = 0L
        withContext(Dispatchers.IO) {
            newId = database.insert(term)
        }
        return newId
    }

    // clears all terms from the database!
    private suspend fun clear(term: TermEntity) {
        withContext(Dispatchers.IO) {
            database.clearTerms()
        }
    }

    /**
     * click handler for add term button
     * inserts new term into the database with a placeholder title and dates
     * navigates to term detail screen for user to edit
      */

    fun onAddTerm() {
        uiScope.launch {
            val newTerm = TermEntity()
            newTerm.termTitle = "New Term"
            val newId = insert(newTerm)
            onTermClicked(newId)
            onTermNavigated()
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

    private val _navigateToMainMenu = MutableLiveData<Boolean>()
    val navigateToMainMenu
        get() = _navigateToMainMenu

    fun onNavigateToMainMenu() {
        _navigateToMainMenu.value = true
    }

    fun onMainMenuNavigated() {
        _navigateToMainMenu.value = null
    }

}