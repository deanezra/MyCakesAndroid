package com.deanezra.android.mycakes.network

/**
 * Data class to combine:
 * Network status - whether API call was successful, error'd or is loading.
 * Data - The API data returned (e.g. cakes in this app)
 * Message - A human readable string message for error state
 */
data class Resource<out T>(
    val status: NetworkStatus,
    val data: T?,
    val message:String?
){
    companion object{

        fun <T> success(data:T?): Resource<T>{
            return Resource(NetworkStatus.SUCCESS, data, null)
        }

        fun <T> error(msg:String, data:T?): Resource<T>{
            return Resource(NetworkStatus.ERROR, data, msg)
        }

        fun <T> loading(data:T?): Resource<T>{
            return Resource(NetworkStatus.LOADING, data, null)
        }

    }
}