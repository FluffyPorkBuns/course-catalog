package com.example.coursecatalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainMenuViewModel() : ViewModel() {

    // tells the app when to navigate via a listener
    private val _navigateToTermList = MutableLiveData<Long>()
    val navigateToTermList
        get() = _navigateToTermList

    // click handler for terms button on main menu
    fun onTermsButtonClicked() {
        _navigateToTermList.value = 1
    }

    // handler for completing navigation
    fun onTermListNavigated() {
        _navigateToTermList.value = null
    }


}