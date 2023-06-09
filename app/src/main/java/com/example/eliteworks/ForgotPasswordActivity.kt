package com.example.eliteworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.eliteworks.databinding.ActivityForgotPasswordBinding
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
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        val email = binding.etEmailFromForgotPassword.text.toString()
        val call = apiService.forgotPassword(email)
        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
              if(response.isSuccessful){
                  val responseBody: ResponseBody? = response.body()
                  if(responseBody!=null){
                      Log.e("TAG",responseBody.string())
                      showErrorSnackBar("Email Sent Successfully",false)
                      startActivity(Intent(this@ForgotPasswordActivity,LoginActivity::class.java))
                  }
              }else{
                  showErrorSnackBar("Server error - " + response.message(),true)
              }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showErrorSnackBar(t.localizedMessage.toString(),true)
                Log.e("TAG",t.localizedMessage.toString())
            }

        })
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