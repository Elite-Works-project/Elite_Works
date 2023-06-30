package com.example.eliteworks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.eliteworks.databinding.ActivityUpdateProfileBinding
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class UpdateProfileActivity : BaseActivity(), OTPVerificationDialog.OTPVerificationListener,
    OTPVerificationDialog.resendOtpListener {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var otpVerificationDialog: OTPVerificationDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        getUserData();
        binding.btnVerifyMobileNumber.setOnClickListener {

            if (binding.etMobileNumberFromUpdateProfile.text.length == 10) {
                sendOtp()
            } else {
                showErrorSnackBar("Enter Valid Number", true)
            }
        }

       //choose image and update image
        binding.userImageUpdateProfile.setOnClickListener {
           showDialog()
        }
    }

    override fun onOTPVerified() {
        // Perform actions after OTP verification
        otpVerificationDialog.dismiss()
        binding.btnVerifyMobileNumber.text = ""
        binding.btnVerifyMobileNumber.isEnabled = false
        binding.etMobileNumberFromUpdateProfile.isEnabled = false
        binding.etCountryCodeFromUpdateProfile.isEnabled = false
        showErrorSnackBar("Mobile number is verified", false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==1){
                val imgUri:Uri = data?.data!!
                if(null!=imgUri){
                    binding.userImageUpdateProfile.setImageURI(imgUri);
                    // send image to database
                }
            }
        }
    }

    fun showDialog(){
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.image_dialog_layout)
        dialog.show()

        val camera = dialog.findViewById<LinearLayout>(R.id.ll_camera)
        val gallery = dialog.findViewById<LinearLayout>(R.id.ll_gallery)

        camera?.setOnClickListener {
            // to be implemented
        }

        gallery?.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent,1)
            dialog.dismiss()
        }
    }
    fun sendOtp() {

        var countryCode = binding.etCountryCodeFromUpdateProfile.text.toString()
        var mobileNumber = binding.etMobileNumberFromUpdateProfile.text.toString()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //credential is verified in OTPVerification Dialog.
            }

            override fun onVerificationFailed(e: FirebaseException) {
//                Log.e("Mobile number", "onVerificationFailed: ${countryCode+mobileNumber}")
                Log.e("OTP", "onVerificationFailed: ", e)
                Toast.makeText(this@UpdateProfileActivity, e.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                otpVerificationDialog = OTPVerificationDialog(
                    this@UpdateProfileActivity,
                    countryCode,
                    mobileNumber,
                    verificationId,
                    this@UpdateProfileActivity
                )
                otpVerificationDialog.setOTPVerificationListener(this@UpdateProfileActivity)
                otpVerificationDialog.setResendOtpListener(this@UpdateProfileActivity)
                otpVerificationDialog.setCancelable(false)
                otpVerificationDialog.show()
            }
        }

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(countryCode + mobileNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallback) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun resendOtp() {
        sendOtp()
    }

    fun getUserData(){
        // get data from database

    }
}