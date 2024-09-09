package com.example.round1_newwave.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: ResponseRepository) : ViewModel() {
    val locations: MutableLiveData<List<Response.Item>> = MutableLiveData()

    fun getLocations(location: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = repository.getLocations(location)
            locations.postValue(response.items)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    class Factory(private val repository: ResponseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LocationViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}