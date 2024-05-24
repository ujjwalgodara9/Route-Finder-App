package com.example.que2_mc_endsem.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteApiService {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>
}
