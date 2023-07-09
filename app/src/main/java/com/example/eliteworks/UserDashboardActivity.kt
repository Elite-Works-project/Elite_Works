package com.example.eliteworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eliteworks.databinding.ActivityUserDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserDashboardBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        getUserDetails()

        binding.btnLogOut.setOnClickListener{
            logoutUser()
        }
    }

    private fun getUserDetails() {
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        binding.textView.text = "Congratulations You've Logged In ${user.name} , ${user.email}"
    }

    private fun logoutUser() {
        mAuth.signOut()
        // Navigate back to the LoginActivity or any other desired destination
        // For example, navigate back to the LoginActivity:
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Optional: finish the current activity to prevent the user from coming back to it using the back button
    }

}