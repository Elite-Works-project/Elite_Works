package com.example.eliteworks

import java.util.Date

data class RegisterRequest(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val phoneNo: String,
    val gender: String,
    val dob: Date,
    val address: String,
    val photo: String
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val phoneNo: String,
    val gender: String,
    val dob: Date,
    val address: String,
    val photo: String
)

data class RegisterResponse(
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String
)