package com.example.eliteworks

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/api/registerUser")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("/api/getUser/{id}")
    fun getUser(@Path("id") id: String): Call<UserResponse>
}