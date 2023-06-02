package com.example.eliteworks

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("registerUser")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<ResponseBody>

    @GET("getUser/{id}")
    fun getUser(@Path("id") id: String): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("loginUser")
    fun loginUser(@Body loginRequest: LoginRequest): Call<ResponseBody>

    @POST("forgot-password")
    fun forgotPassword(@Query("email") email: String): Call<ResponseBody>

}