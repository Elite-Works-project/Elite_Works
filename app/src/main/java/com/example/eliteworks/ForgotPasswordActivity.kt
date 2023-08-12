package com.example.eliteworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.eliteworks.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : BaseActivity()
{
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSubmitFromForgotPassword.setOnClickListener{
            if(emailIDValidated())
            {
                 //reset password mail will be sent to Email ID
                //TODO: Reseting password from email [link] doesn't reset in FireStore
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        val email = binding.etEmailFromForgotPassword.text.toString()

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Email sent.")
                    showErrorSnackBar("Email Sent Successfully", false)
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                } else {
                    showErrorSnackBar("Password reset failed. " + task.exception?.message, true)
                }
            }
    }


    private fun emailIDValidated(): Boolean
    {
        return when {
            TextUtils.isEmpty(binding.etEmailFromForgotPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmailFromForgotPassword.text.toString().trim { it <= ' ' }).matches() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_invalid_email), true)
                false
            }
            else ->{
                true
            }
        }

    }
}