package com.example.eliteworks

import android.os.Bundle
import com.example.eliteworks.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnVerifyMobileNumber.setOnClickListener {

            if (binding.etMobileNumberFromUpdateProfile.text.length == 10) {
                var countryCode = binding.etCountryCodeFromUpdateProfile.text.toString()
                var mobileNumber = binding.etMobileNumberFromUpdateProfile.text.toString()
                var otpVerificationDialog = OTPVerificationDialog(this, countryCode, mobileNumber)
                otpVerificationDialog.setCancelable(false)
                otpVerificationDialog.show()
            } else {
                showErrorSnackBar("Enter Valid Number", true)
            }

        }
    }
}