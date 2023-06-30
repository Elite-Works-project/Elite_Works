package com.example.eliteworks

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.eliteworks.databinding.OtpDialogLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPVerificationDialog(
    context: Context,
    private val countryCode: String,
    private val mobileNumber: String,
    private val verificationId: String,
    private val updateProfileContext: Context
) :
    Dialog(context) {

    private lateinit var binding: OtpDialogLayoutBinding
    private var resendTime = 60
    private var resendEnabled = false
    private var selectedTextBoxPosition = 1
    private lateinit var credential: PhoneAuthCredential
    private lateinit var mAuth: FirebaseAuth

    interface OTPVerificationListener {
        fun onOTPVerified()
    }

    interface resendOtpListener {
        fun resendOtp()
    }

    private var otpVerificationListener: OTPVerificationListener? = null
    private var resendOTPListener: resendOtpListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OtpDialogLayoutBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()
        // When the pop-up appears, the keyboard will appear automatically
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        window?.setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent)))

        setContentView(binding.root)

        binding.otpEt1.addTextChangedListener(textWatcher)
        binding.otpEt2.addTextChangedListener(textWatcher)
        binding.otpEt3.addTextChangedListener(textWatcher)
        binding.otpEt4.addTextChangedListener(textWatcher)
        binding.otpEt5.addTextChangedListener(textWatcher)
        binding.otpEt6.addTextChangedListener(textWatcher)

        binding.tvMobileNumberFromOtpVerification.text = "$countryCode $mobileNumber"

        // By default, the cursor should be in the 1st text box
        showKeyboard(binding.otpEt1)

        startCountDownTimer()

        binding.btnResendOtpVerification.setOnClickListener {
            if (resendEnabled) {
                // Resend code
                resendOTPListener?.resendOtp()
                startCountDownTimer()
            }
        }

        binding.btnVerify.setOnClickListener {
            val userEnteredOTP =
                binding.otpEt1.text.toString() + binding.otpEt2.text.toString() + binding.otpEt3.text.toString() + binding.otpEt4.text.toString() + binding.otpEt5.text.toString() + binding.otpEt6.text.toString()

            if (userEnteredOTP.length == 6) {
                verifyOTP(verificationId, userEnteredOTP)
            } else {
                Toast.makeText(context, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOTP(verificationId: String, userEnteredOTP: String) {
        //verify
        Log.e("OTP", "verificationId : $verificationId , Otp : $userEnteredOTP")
        credential = PhoneAuthProvider.getCredential(verificationId!!, userEnteredOTP)
        signInWithPhoneAuthCredential(credential)
    }


    private fun showKeyboard(otpEt: EditText) {
        // Changing focus of cursor to the text box
        if (otpEt.requestFocus()) {
            val inputMethodManager = context.getSystemService(InputMethodManager::class.java)
            inputMethodManager.showSoftInput(otpEt, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // No implementation
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // No implementation
        }

        override fun afterTextChanged(s: Editable?) {
            val otpLength =
                binding.otpEt1.length() + binding.otpEt2.length() + binding.otpEt3.length() + binding.otpEt4.length()

            if (otpLength == 6) {
                binding.btnVerify.setBackgroundResource(R.drawable.otp_verify_red_bg)
            } else {
                binding.btnVerify.setBackgroundResource(R.drawable.otp_verify_brown_bg)
            }

            if (s!!.length > 0) {
                if (selectedTextBoxPosition == 1) {
                    selectedTextBoxPosition = 2
                    showKeyboard(binding.otpEt2)
                } else if (selectedTextBoxPosition == 2) {
                    selectedTextBoxPosition = 3
                    showKeyboard(binding.otpEt3)
                } else if (selectedTextBoxPosition == 3) {
                    selectedTextBoxPosition = 4
                    showKeyboard(binding.otpEt4)
                } else if (selectedTextBoxPosition == 4) {
                    selectedTextBoxPosition = 5
                    showKeyboard(binding.otpEt5)
                } else if (selectedTextBoxPosition == 5) {
                    selectedTextBoxPosition = 6
                    showKeyboard(binding.otpEt6)
                } else if (selectedTextBoxPosition == 6) {
                    binding.btnVerify.setBackgroundResource(R.drawable.otp_verify_red_bg)
                }
            }
        }
    }

    private fun startCountDownTimer() {
        resendEnabled = false
        binding.btnResendOtpVerification.setTextColor(Color.BLACK)

        val countDownTimer = object : CountDownTimer((resendTime * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnResendOtpVerification.text =
                    "Resend Code (${millisUntilFinished / 1000})"
            }

            override fun onFinish() {
                resendEnabled = true
                binding.btnResendOtpVerification.text = "Resend Code"
                binding.btnResendOtpVerification.setTextColor(context.resources.getColor(android.R.color.holo_blue_dark))
            }
        }

        countDownTimer.start()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        // If we delete the digit from the EditText, then the cursor and color of the verify button should be changed
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (selectedTextBoxPosition == 6) {
                selectedTextBoxPosition = 5
                showKeyboard(binding.otpEt5)
            } else if (selectedTextBoxPosition == 5) {
                selectedTextBoxPosition = 4
                showKeyboard(binding.otpEt4)
            } else if (selectedTextBoxPosition == 4) {
                selectedTextBoxPosition = 3
                showKeyboard(binding.otpEt3)
            } else if (selectedTextBoxPosition == 3) {
                selectedTextBoxPosition = 2
                showKeyboard(binding.otpEt2)
            } else if (selectedTextBoxPosition == 2) {
                selectedTextBoxPosition = 1
                showKeyboard(binding.otpEt1)
            }


            binding.btnVerify.setBackgroundResource(R.drawable.otp_verify_brown_bg)
            return true
        }

        return super.onKeyUp(keyCode, event)
    }

    fun setOTPVerificationListener(listener: OTPVerificationListener) {
        otpVerificationListener = listener
    }

    fun setResendOtpListener(listener: resendOtpListener) {
        resendOTPListener = listener
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
                    otpVerificationListener?.onOTPVerified()
//                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(updateProfileContext, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.e("OTP", "signInWithPhoneAuthCredential: ", task.exception)
                    }
                    // Update UI
                }
            }
    }

}
