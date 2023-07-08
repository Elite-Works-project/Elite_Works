package com.example.eliteworks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.eliteworks.databinding.ActivityUpdateProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.io.IOException
import java.util.concurrent.TimeUnit

open class UpdateProfileActivity : BaseActivity(), OTPVerificationDialog.OTPVerificationListener,
    OTPVerificationDialog.resendOtpListener{
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var otpVerificationDialog: OTPVerificationDialog
    private var mUserImageUri: Uri? =null
    private var mUser = User()
    private var isNumberVerified = false
    private var mUserProfileImageURL: String ?= null
    val userHashMap = HashMap<String,Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS))
        {
            mUser = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }


        binding.etNameFromUpdateProfile.setText(mUser.name)
        binding.etEmailFromUpdateProfile.setText(mUser.email)
        binding.etEmailFromUpdateProfile.isEnabled = false

        if(!mUser.profileCompleted)
        {
            // complete profile

            binding.etNameFromUpdateProfile.isEnabled = false
        }
        else
        {
            GlideLoader(this).loadUserPicture(mUser.photo,binding.userImageUpdateProfile)

            binding.etMobileNumberFromUpdateProfile.setText(mUser.phoneNo)
            binding.etMobileNumberFromUpdateProfile.setText(mUser.address)

            if(mUser.gender== Constants.MALE)
            {
                binding.maleRadioButton.isChecked = true
            }
            else
            {
                binding.femaleRadioButton.isChecked = true
            }
        }


        binding.btnVerifyMobileNumber.setOnClickListener {

            if (binding.etMobileNumberFromUpdateProfile.text.length == 10) {
                sendOtp()
            } else {
                showErrorSnackBar("Enter Valid Number", true)
            }
        }

       //choose image and update image
        binding.uploadImageUpdateProfile.setOnClickListener {
           showDialog()
        }

        binding.userImageUpdateProfile.setOnClickListener {

            if(mUserImageUri != null)
            {
                val intent = Intent(this,ProfilePreviewActivity::class.java)
                //here add changeImageDialogListener
                intent.putExtra("userImageUri",mUserImageUri)
                startActivity(intent)
            }
        }

        binding.btnSaveFromUpdateProfile.setOnClickListener{
            if(!isNumberVerified)
            {
                showErrorSnackBar("Please Verify Mobile Number",true)
            }
            else
            {
                if(mUserImageUri!=null)
                {
                    FirestoreClass().uploadImageToCloudStorage(this,mUserImageUri,Constants.USER_PROFILE_IMAGE)

                    if(mUserProfileImageURL!=null)
                    {
                        userHashMap[Constants.PHOTO] = mUserProfileImageURL!!
                    }
                }

                updateUserProfileDetails()
            }
        }

    }

    private fun updateUserProfileDetails() {
        val name = binding.etNameFromUpdateProfile.text.toString().trim{ it <= ' '}
        if(name != mUser.name)
        {
            userHashMap[Constants.FIRST_NAME] = name
        }

        val mobileNumber = binding.etCountryCodeFromUpdateProfile.text
        mobileNumber.append(binding.etMobileNumberFromUpdateProfile.text.toString().trim{it <= ' '})
        val gender = if(binding.femaleRadioButton.isChecked)
        {
            Constants.FEMALE
        }
        else{
            Constants.MALE
        }

        if(mUserProfileImageURL!=null)
        {
            userHashMap[Constants.PHOTO] = mUserProfileImageURL!!
        }
        userHashMap[Constants.PHONENO] = mobileNumber
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = 1
//        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().updateUserDetails(this, userHashMap)
    }

    override fun onOTPVerified() {
        // Perform actions after OTP verification
        otpVerificationDialog.dismiss()
        binding.btnVerifyMobileNumber.text = ""
        binding.btnVerifyMobileNumber.isEnabled = false
        binding.etMobileNumberFromUpdateProfile.isEnabled = false
        binding.etCountryCodeFromUpdateProfile.isEnabled = false
        isNumberVerified = true
        showErrorSnackBar("Mobile number is verified", false)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==1){
                if(data!=null)
                {
                    try {
                        mUserImageUri = data.data!!
                        binding.userImageUpdateProfile.setImageURI(mUserImageUri)
                    }
                    catch (e: IOException)
                    {
                        e.printStackTrace()
                        Toast.makeText(this, "Image Selection Failed!", Toast.LENGTH_SHORT).show()
                    }
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

    fun imageUploadSuccess(imageRL:String)
    {
//        hideProgressDialog()
        mUserProfileImageURL = imageRL

        // as in any case we have to update the user details although we haven't selected a profile image.
        updateUserProfileDetails()
        Log.e("imageUploadSuccess: ", mUserProfileImageURL!!)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,UserDashboardActivity::class.java))
        finish()
    }
}