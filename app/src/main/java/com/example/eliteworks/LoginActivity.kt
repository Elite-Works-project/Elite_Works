package com.example.eliteworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.example.eliteworks.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSignupFromLogin.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        binding.btnForgotPasswordFromLogin.setOnClickListener{
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

        binding.btnLoginFromLogin.setOnClickListener{
            loginUser()
        }
    }

    private fun loginUser() {

        if(loginDetailsValidated())
        {
            showErrorSnackBar("Validated",false)

            //Log in through from here
        }
    }

    private fun loginDetailsValidated(): Boolean
    {
        return when {
            TextUtils.isEmpty(binding.etEmailFromLogin.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmailFromLogin.text.toString().trim { it <= ' ' }).matches() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_invalid_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPasswordFromLogin.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }
}