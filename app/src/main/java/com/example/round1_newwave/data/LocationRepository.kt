package com.example.round1_newwave.data

class ResponseRepository(private val apiService: ApiService) {
    suspend fun getLocations(location: String): Response {
        return apiService.getLocations(location)
    }
}