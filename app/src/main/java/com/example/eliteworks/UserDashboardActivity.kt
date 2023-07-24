package com.example.eliteworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eliteworks.databinding.ActivityUserDashboardBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

//private var currentFragment:Fragment = HomeFragment()
class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserDashboardBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        replaceFragment(currentFragment)
        mAuth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_dashboard) as NavHostFragment
        navController = navHostFragment.navController
        Log.e("NavController", "onCreate: $navController", )

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView_user_dashboard)
        val layout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // This line gives error
        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
        layout.setupWithNavController(toolbar,navController,appBarConfiguration)


//        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    private fun replaceFragment(fragment: Fragment)
//    {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frameLayout_user_dashboard,fragment)
//        fragmentTransaction.commit()
//    }

}