package com.deanezra.android.mycakes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deanezra.android.mycakes.R
import com.deanezra.android.mycakes.databinding.ActivityMainBinding
import com.deanezra.android.mycakes.models.Cake
import com.deanezra.android.mycakes.network.NetworkStatus
import com.deanezra.android.mycakes.reposiitories.CakeRepository
import com.deanezra.android.mycakes.services.RetrofitService
import com.deanezra.android.mycakes.viewmodels.MainViewModel
import com.deanezra.android.mycakes.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    val adapter = MainAdapter(MainAdapter.OnClickListener { cake ->
        showCakeDescriptionDialog(cake)
    })

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_refresh -> {
                Toast.makeText(applicationContext,
                    "Refreshing cake list..",
                    Toast.LENGTH_SHORT).show()
                viewModel.getAllCakes()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
            Log.d(TAG, "Error occurred getting cakes from API: $it")

            // TODO: Would be better to use a Material design error popup dialog
            // With title, icon and error in dialog main body.
            val text = "Cake list could not be populated. $it"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        })

        viewModel.networkStatus.observe(this, {
            when (it) {
                NetworkStatus.ERROR, NetworkStatus.SUCCESS -> {
                    binding.spinner.visibility = View.GONE
                    binding.recyclerview.visibility = View.VISIBLE
                }
                NetworkStatus.LOADING -> {
                    binding.spinner.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.VISIBLE
                }
            }
        })
        viewModel.getAllCakes()
    }

    fun showCakeDescriptionDialog(cake:Cake) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(cake.title)
        alertDialogBuilder.setMessage(cake.desc)

        alertDialogBuilder.create().show()
    }
}