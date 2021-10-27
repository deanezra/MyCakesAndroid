package com.deanezra.android.mycakes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deanezra.android.mycakes.R
import com.deanezra.android.mycakes.databinding.ActivityMainBinding
import com.deanezra.android.mycakes.reposiitories.CakeRepository
import com.deanezra.android.mycakes.services.RetrofitService
import com.deanezra.android.mycakes.viewmodels.MainViewModel
import com.deanezra.android.mycakes.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(CakeRepository(retrofitService))).get(MainViewModel::class.java)

        binding.recyclerview.adapter = adapter

        viewModel.cakeList.observe(this, Observer {
            Log.d(TAG, "onCreate: VM.CakeList observer fired with $it")
            adapter.setCakeList(it)
        })

        viewModel.errorMessage.observe(this, Observer {

        })
        viewModel.getAllCakes()
    }
}