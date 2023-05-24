package com.example.eliteworks

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val gson = GsonBuilder()
    .setLenient()
    .create()

val retrofit = Retrofit.Builder()
    .baseUrl("https://eliteworks.azurewebsites.net/api/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

val apiService = retrofit.create(ApiService::class.java)