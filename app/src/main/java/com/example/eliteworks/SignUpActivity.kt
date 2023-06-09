package com.example.eliteworks



import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Patterns
import com.example.eliteworks.databinding.ActivitySignUpBinding
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

import java.util.Date
import java.util.UUID

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLoginFromSignup.setOnClickListener {
            onBackPressed()
        }

        binding.btnSignupFromSignup.setOnClickListener{
            signUpUser()
        }
    }

    private fun signUpUser() {

        if(signUpDetailsValidated())
        {
//            showErrorSnackBar("Validated",false)
            // Add User to Database from here and also check if user is already exist
            registerUserApi()

        }
    }

    private fun registerUserApi() {
        val name = binding.etFirstNameFromSignup.text.toString() + " " + binding.etLastNameFromSignup.text.toString()
        val email = binding.etEmailFromSignup.text.toString()
        val password = binding.etPasswordFromSignup.text.toString()

        val registerRequest = RegisterRequest(UUID.randomUUID().toString(),name,email,password,"","","","","","",false,"","");
        val call = apiService.registerUser(registerRequest)

        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful){
                    val responseBody :ResponseBody? = response.body()
                    if (responseBody != null) {
//                        showErrorSnackBar(responseBody.toString(),false)
                        // add code if registration successful change activity and progress bar
                        startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                    }
                }else{
                    // add code if server error
                    Log.e("Signup Error",response.body().toString())
                    showErrorSnackBar(response.message(),true)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // add code if failure
                showErrorSnackBar(t.localizedMessage.toString(),true)
                Log.e("TAG",t.localizedMessage.toString())
            }
        })
    }

    private fun signUpDetailsValidated(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFirstNameFromSignup.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(binding.etLastNameFromSignup.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(binding.etEmailFromSignup.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmailFromSignup.text.toString().trim { it <= ' ' }).matches() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_invalid_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPasswordFromSignup.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            binding.etPasswordFromSignup.text.toString().trim { it <= ' ' }.length < 6 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_length), true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPasswordFromSignup.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            binding.etPasswordFromSignup.text.toString().trim { it <= ' ' } != binding.etConfirmPasswordFromSignup.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            else -> true
        }
    }
}