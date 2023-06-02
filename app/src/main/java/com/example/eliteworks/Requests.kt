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
    val dob: String,
    val address: String,
    val photo: String,
    val isProfileCompleted: Boolean,
    val token: String,
    val tokenDate: String,
)

data class LoginRequest(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val phoneNo: String,
    val gender: String,
    val dob: String,
    val address: String,
    val photo: String,
    val isProfileCompleted: Boolean,
    val token: String,
    val tokenDate: String,
)
