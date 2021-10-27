package com.deanezra.android.mycakes.reposiitories

import com.deanezra.android.mycakes.services.RetrofitService

class CakeRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getAllCakes() = retrofitService.getAllCakes()
}