package com.example.eliteworks

import android.os.Bundle

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.eliteworks.databinding.ActivityUserDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityUserDashboardBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_dashboard) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView_user_dashboard)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.bottom_nav_bar_home -> {
                    navController.navigate(R.id.navigation_Home)
                    true
                }
                R.id.bottom_nav_bar_completed -> {
                    navController.navigate(R.id.navigation_completed)
                    true
                }
                R.id.bottom_nav_bar_leaves -> {
                    navController.navigate(R.id.navigation_leaves)
                    true
                }
                R.id.bottom_nav_bar_attendance -> {
                    navController.navigate(R.id.navigation_attendance)
                    true
                }
                R.id.bottom_nav_bar_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.navigation_Home) {
            navController.popBackStack(R.id.navigation_Home, false)
            bottomNavigationView.menu.findItem(R.id.bottom_nav_bar_home).isChecked = true
        } else {
            doubleBackToExit()
        }
    }


}
