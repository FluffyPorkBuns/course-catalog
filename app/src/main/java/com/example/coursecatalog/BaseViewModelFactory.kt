package com.example.coursecatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Viewmodel factory that can be used for multiple ViewModels in the app
 */

class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}