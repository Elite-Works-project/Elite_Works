package com.example.eliteworks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.eliteworks.databinding.ActivityCompleteProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.io.IOException
import java.util.concurrent.TimeUnit

open class CompleteProfileActivity : BaseActivity(), OTPVerificationDialog.OTPVerificationListener,
    OTPVerificationDialog.resendOtpListener{
    private lateinit var binding: ActivityCompleteProfileBinding
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var otpVerificationDialog: OTPVerificationDialog
    private var mUserImageUri: Uri? =null
    private var mUser = User()

    //TODO : Change to false after testing mode
    private var isNumberVerified = true

    private var mUserProfileImageURL: String ?= null
    val userHashMap = HashMap<String,Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompleteProfileBinding.inflate(layoutInflater)
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
            if(detailsValidate())
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

    private fun detailsValidate(): Boolean
    {
        if(!isNumberVerified)
        {
            showErrorSnackBar("Please Verify Mobile Number",true)
            return false
        }

        if(TextUtils.isEmpty(binding.etAddressFromUpdateProfile.text.toString().trim{it <= ' '}))
        {
            showErrorSnackBar("Please Enter Address",true)
            return false
        }
        return true
    }

    private fun updateUserProfileDetails() {
        val name = binding.etNameFromUpdateProfile.text.toString().trim{ it <= ' '}
        if(name != mUser.name)
        {
            userHashMap[Constants.NAME] = name
        }

        val countryCode = binding.etCountryCodeFromUpdateProfile.text.toString()
        val phoneNo = binding.etMobileNumberFromUpdateProfile.text.toString()
        val mobileNumber = countryCode+phoneNo
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
        else{
            userHashMap[Constants.PHOTO]  = ""
        }

        val address = binding.etAddressFromUpdateProfile.text.toString().trim{it <=' '}
        userHashMap[Constants.ADDRESS] = address

        userHashMap[Constants.PHONENO] = mobileNumber
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = true

        // these fields remains same
        userHashMap[Constants.EMAIL] = mUser.email
        userHashMap[Constants.ID] = mUser.id
        userHashMap[Constants.PASSWORD] = mUser.password
        userHashMap[Constants.ROLE] = mUser.role

//        showProgressDialog(resources.getString(R.string.please_wait))
        Log.e("UserMap", "updateUserProfileDetails: $userHashMap", )
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
                Toast.makeText(this@CompleteProfileActivity, e.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                otpVerificationDialog = OTPVerificationDialog(
                    this@CompleteProfileActivity,
                    countryCode,
                    mobileNumber,
                    verificationId,
                    this@CompleteProfileActivity
                )
                otpVerificationDialog.setOTPVerificationListener(this@CompleteProfileActivity)
                otpVerificationDialog.setResendOtpListener(this@CompleteProfileActivity)
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
    }
}