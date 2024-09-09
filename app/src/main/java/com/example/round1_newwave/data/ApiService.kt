package com.example.round1_newwave.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://discover.search.hereapi.com/v1/").addConverterFactory(
    GsonConverterFactory.create()).build()

interface ApiService {
    @GET("discover?at=40.7307999,-73.9973085&limit=2&apiKey=2BRSOkHqKlh6JeINVXC1ok5_4fg_hxzVzSqrYTj9tQg")
    suspend fun getLocations(
        @Query("q") location: String
    ): Response
}

val apiService: ApiService = retrofit.create(ApiService::class.java)