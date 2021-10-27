package com.deanezra.android.mycakes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deanezra.android.mycakes.models.Cake
import com.deanezra.android.mycakes.reposiitories.CakeRepository
import androidx.lifecycle.viewModelScope
import com.deanezra.android.mycakes.network.NetworkStatus
import kotlinx.coroutines.launch

class MainViewModel constructor(private val repository: CakeRepository)  : ViewModel() {

    val cakeList = MutableLiveData<List<Cake>>()
    val errorMessage = MutableLiveData<String>()
    val networkStatus = MutableLiveData<NetworkStatus>()

    fun getAllCakes()  = viewModelScope.launch {
        networkStatus.postValue(NetworkStatus.LOADING)

        repository.getAllCakes().let {
            if (it.isSuccessful){

                it.body()?.let {
                    // Remove duplicate cakes by title field:
                    var distinctCakes = it.distinctBy { it.title }
                    cakeList.postValue(distinctCakes)
                }

                networkStatus.postValue(NetworkStatus.SUCCESS)
            }else{
                // We will leave the cake list contents as is. But we will set error state and msg
                networkStatus.postValue(NetworkStatus.ERROR)
                errorMessage.postValue(it.errorBody().toString())
            }
        }
    }

    /*
    fun getAllCakes() {

        val response = repository.getAllCakes()
        response.enqueue(object : Callback<List<Cake>> {
            override fun onResponse(call: Call<List<Cake>>, response: Response<List<Cake>>) {
                cakeList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Cake>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    */
}