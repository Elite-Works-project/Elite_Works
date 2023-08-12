package com.example.eliteworks

import android.content.Intent
import android.os.Bundle
import com.example.eliteworks.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var mUser:User
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirestoreClass().getUserDetails(this)
//        Toast.makeText(this, mUser.email, Toast.LENGTH_SHORT).show()
        binding.btnForgotPasswordFromChangePassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }

    private fun updatePasswordInFireStore(newPassword: String)
    {
        var updatedPassword = HashMap<String,Any>()
        updatedPassword[Constants.PASSWORD] = newPassword

        FirestoreClass().updateUserDetails(this,updatedPassword)
        showErrorSnackBar("Password Updated",false)
    }

    fun onDetailsGetSuccess(user:User)
    {
        mUser = user
        binding.btnChangePasswordFromChangePassword.setOnClickListener{
            if(mUser!=null)
            {
                var currentPassword = binding.etCurrentPasswordFromChangePassword.text.toString().trim{ it <= ' '}

                if(currentPassword == mUser.password)
                {
                    val newPassword = binding.etNewPasswordFromChangePassword.text.toString().trim{it <= ' '}
                    val confirmPassword = binding.etConfirmPasswordFromChangePassword.text.toString().trim{it <= ' '}
                    if(newPassword.length < 6)
                    {
                        showErrorSnackBar(resources.getString(R.string.err_msg_password_length),true)
                    }
                    else
                    {
                        if(newPassword == mUser.password)
                        {
                            showErrorSnackBar("New Password and Current Password can not be same",true)
                        }
                        else if(newPassword != confirmPassword)
                        {
                            showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                        }
                        else
                        {
                            updatePasswordInFireStore(newPassword)
                        }
                    }
                }
            }
        }
    }

}