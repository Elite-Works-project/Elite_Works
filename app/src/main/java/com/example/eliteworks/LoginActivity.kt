package com.example.eliteworks

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.eliteworks.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mUser:FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        //if already log in then redirect to page



        binding.btnSignupFromLogin.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        binding.btnForgotPasswordFromLogin.setOnClickListener{
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

        binding.btnLoginFromLogin.setOnClickListener{
            showProgressDialog("Please wait...")
            validateAndLoginUser()
        }
    }

    private fun validateAndLoginUser() {

        if(loginDetailsValidated())
        {
            //Log in through here
//            showErrorSnackBar("Validated",false)
            loginUser()
        }
    }

    private fun loginUser() {
        val email = binding.etEmailFromLogin.text.toString().trim{it <= ' '}
        val password = binding.etPasswordFromLogin.text.toString().trim{it <= ' '}

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful)
                {
                    FirestoreClass().getUserDetails(this)
                }
                else
                {
                    hideProgressDialog()
                    if(!Constants.isNetworkConnected(this))
                    {
                        showErrorSnackBar("Sorry, unable to login. Please check your internet connection.", true)
                    }
                }
            }
            .addOnFailureListener { e->
                hideProgressDialog()
                if(!Constants.isNetworkConnected(this))
                {
                    showErrorSnackBar("Sorry, unable to login. Please check your internet connection.", true)
                }
                else
                {
                    showErrorSnackBar("Incorrect Email ID or Password", true)
                }
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

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()

        Log.i("First Name:",user.name)
        Log.i("Email:",user.email)

        if(!user.profileCompleted)
        {
            val intent = Intent(this, CompleteProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else {
            startActivity(Intent(this, UserDashboardActivity::class.java))
        }
        finish()
    }
}