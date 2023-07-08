package com.example.eliteworks

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.eliteworks.databinding.ActivityProfilePreviewBinding

class ProfilePreviewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfilePreviewBinding
    private lateinit var mUserImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserImageUri = intent.getParcelableExtra("userImageUri")!!

        // Change status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Change navigation bar color
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        setupActionBar()
        setUserImage()
    }

    private fun setUserImage() {
        binding.fullScreenProfileImageView.setImageURI(mUserImageUri)
    }

    private fun setupActionBar()
    {
        setSupportActionBar(binding.toolbarProfilePhotoActivity)

        val actionbar = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            setTitle("Profile Image")
        }
        binding.toolbarProfilePhotoActivity.setNavigationOnClickListener{ onBackPressed()}
    }

    fun userDetailsSuccess(user: User) {

    }
}