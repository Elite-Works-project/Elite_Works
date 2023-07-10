package com.example.eliteworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
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
        replaceFragment(HomeFragment())
        getUserDetails()

        binding.bottomNavigationViewUserDashboard.setOnItemSelectedListener {item->

            when(item.itemId){
                R.id.bottom_nav_bar_home -> replaceFragment(HomeFragment())
                R.id.bottom_nav_bar_completed -> replaceFragment(CompletedFragment())
                R.id.bottom_nav_bar_leaves -> replaceFragment(LeavesFragment())
                R.id.bottom_nav_bar_salary -> replaceFragment(SalaryFragment())
                R.id.bottom_nav_bar_profile -> replaceFragment(ProfileFragment())
                else ->{

                }
            }
            true

        }

//        binding.btnLogout.setOnClickListener{
//            logoutUser()
//        }
    }

    private fun getUserDetails() {
        FirestoreClass().getUserDetails(this)
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout_user_dashboard,fragment)
        fragmentTransaction.commit()
    }

//    fun userDetailsSuccess(user: User) {
//        binding.nameUserDashboard.text = user.name
//
//        if(user.photo!="")
//        {
//            GlideLoader(this).loadUserPicture(user.photo,binding.imageUserDashboard)
//        }
//    }

    private fun logoutUser() {
        mAuth.signOut()
        // Navigate back to the LoginActivity or any other desired destination
        // For example, navigate back to the LoginActivity:
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Optional: finish the current activity to prevent the user from coming back to it using the back button
    }

}