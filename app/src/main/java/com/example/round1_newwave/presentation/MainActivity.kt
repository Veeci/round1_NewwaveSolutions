 package com.example.round1_newwave.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.round1_newwave.R
import com.example.round1_newwave.data.LocationAdapter
import com.example.round1_newwave.data.LocationViewModel
import com.example.round1_newwave.data.ResponseRepository
import com.example.round1_newwave.data.apiService
import com.example.round1_newwave.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

 class MainActivity : AppCompatActivity() {

     private lateinit var binding: ActivityMainBinding

     private lateinit var fusedLocationClient: FusedLocationProviderClient

     private val viewModel: LocationViewModel by viewModels {
         LocationViewModel.Factory(ResponseRepository(apiService))
     }

     private lateinit var locationAdapter: LocationAdapter

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()

         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)

         ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
             val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
             v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
             insets
         }

         fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

         setupRecyclerView()

         observeViewModel()
     }

     private fun setupRecyclerView() {
         locationAdapter = LocationAdapter(emptyList()) { location ->
             getCurrentLocation {
                 val uri = "google.navigation:q=${location.lat},${location.lng}&mode=d"
                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                 intent.setPackage("com.google.android.apps.maps")
                 startActivity(intent)
             }
         }

         binding.recyclerView.apply {
             layoutManager = LinearLayoutManager(this@MainActivity)
             adapter = locationAdapter
         }

         viewModel.locations.observe(this, Observer { locations ->
             if (locations.isNotEmpty()) {
                 binding.recyclerView.visibility = View.VISIBLE
                 val result = locations.take(10)
                 locationAdapter.updateLocations(result.map { it })
             } else {
                 binding.recyclerView.visibility = View.GONE
             }
         })

     }

     private fun observeViewModel() {
         binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
             override fun onQueryTextSubmit(query: String?): Boolean {
                 query?.let {
                     viewModel.getLocations(it)
                 }
                 return true
             }

             override fun onQueryTextChange(newText: String?): Boolean {
                 newText?.let {
                     if (it.isNotEmpty()) {
                         viewModel.getLocations(it)
                     } else {
                         binding.recyclerView.visibility = View.GONE
                     }
                 }
                 return true
             }
         })
     }

     private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
             ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
             return
         }

         fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
             if (task.isSuccessful && task.result != null) {
                 onSuccess(task.result!!)
             } else {
                 // Handle the case where location is not available
                 // You might want to use another method to get location or handle the error
             }
         }
     }
 }