package com.example.eliteworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eliteworks.databinding.ActivityUserDashboardBinding

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getUserDetails()
    }

    private fun getUserDetails() {
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        binding.textView.text = "Congratulations You've Logged In ${user.name} , ${user.email}"
    }
}