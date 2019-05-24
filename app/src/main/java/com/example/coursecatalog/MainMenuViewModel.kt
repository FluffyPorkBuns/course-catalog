package com.example.coursecatalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainMenuViewModel : ViewModel() {

    // tells the app when to navigate via a listener
    private val _navigateToTermList = MutableLiveData<Boolean?>()
    val navigateToTermList: LiveData<Boolean?>
        get() = _navigateToTermList

    // click handler for terms button on main menu
    fun onTermsButtonClicked() {
        _navigateToTermList.value = true
    }

    // handler for completing navigation
    fun onTermListNavigated() {
        _navigateToTermList.value = null
    }


}