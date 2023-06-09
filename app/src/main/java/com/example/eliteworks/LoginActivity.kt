package com.example.eliteworks

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.eliteworks.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        //if already log in then redirect to page

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
//            showErrorSnackBar("Validated",false)
            loginUserApi()

            //Log in through from here
        }
    }

    private fun loginUserApi() {
        val email = binding.etEmailFromLogin.text.toString()
        val password = binding.etPasswordFromLogin.text.toString()

        val loginRequest = LoginRequest("","",email,password,"","","","","","",false,"","")
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val responseBody : ResponseBody? = response.body()
                    if(responseBody!=null){
                        Log.e("TAG",responseBody.string())
//                        showErrorSnackBar("Login Successful" + responseBody.string(),false)
                        // add code if login successful change activity and progress bar

                        startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
                    }
                }else{
                    // add code if server error
                    showErrorSnackBar("Server error - " + response.message(),true)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // add code if failure
                showErrorSnackBar(t.localizedMessage.toString(),true)
                Log.e("TAG",t.localizedMessage.toString())
            }

        })
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