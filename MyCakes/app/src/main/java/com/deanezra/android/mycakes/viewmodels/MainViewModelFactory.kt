package com.deanezra.android.mycakes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deanezra.android.mycakes.reposiitories.CakeRepository

/**
 * Factory class to allow app to get hold of View Models and pass the repository via contructor param.
 */
class MainViewModelFactory constructor(private val repository: CakeRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}