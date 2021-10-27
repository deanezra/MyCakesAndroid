package com.deanezra.android.mycakes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deanezra.android.mycakes.models.Cake
import com.deanezra.android.mycakes.reposiitories.CakeRepository
import androidx.lifecycle.viewModelScope
import com.deanezra.android.mycakes.network.NetworkStatus
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainViewModel constructor(private val repository: CakeRepository)  : ViewModel() {

    val cakeList = MutableLiveData<List<Cake>>()
    val errorMessage = MutableLiveData<String>()
    val networkStatus = MutableLiveData<NetworkStatus>()

    fun getAllCakes()  = viewModelScope.launch {
        networkStatus.postValue(NetworkStatus.LOADING)

            repository.getAllCakes().let {
                if (it.isSuccessful) {

                    it.body()?.let {
                        // Remove duplicate cakes by title field and then sort remaining cakes
                        // by title in alphabetical order (Ascending):
                        cakeList.postValue(it.distinctBy { it.title }.sortedBy { it.title })
                    }
                    networkStatus.postValue(NetworkStatus.SUCCESS)
                } else {
                    // We will leave the cake list contents as is. But we will set error state and msg
                    networkStatus.postValue(NetworkStatus.ERROR)

                    // TODO: Add better error checking (it.errorBody).toString() doesn't give a readable message.
                    errorMessage.postValue("There was an error calling the Cakes API. Server responded with $it.code()")
                    //errorMessage.postValue(it.errorBody().toString())
                }
            }

    }
}