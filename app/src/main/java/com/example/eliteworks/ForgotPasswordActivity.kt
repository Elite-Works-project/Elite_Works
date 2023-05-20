package com.example.eliteworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.example.eliteworks.databinding.ActivityForgotPasswordBinding

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