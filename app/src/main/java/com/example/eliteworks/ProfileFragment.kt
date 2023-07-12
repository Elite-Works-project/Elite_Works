package com.example.eliteworks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eliteworks.databinding.FragmentHomeBinding
import com.example.eliteworks.databinding.FragmentProfileBinding

private var mUser: User? = null
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load user data only if it hasn't been loaded before
        if (mUser == null) {
            FirestoreClass().getUserDetailsFragment(this) { user ->
                mUser = user
                Log.i("Load Data", "onCreate: Loaded Profile Data 1st time")
                userDetailsSuccess(user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Display user data if it has been loaded
        mUser?.let { userDetailsSuccess(it) }

        binding.btnSignOutFragment.setOnClickListener{
            //TODO : Logout
        }

        return root
    }

    private fun userDetailsSuccess(user: User) {
        Glide.with(this).load(user.photo).into(binding.imageUserDashboard)
        binding.nameProfileFragment.text=user.name
        binding.phoneProfileFragment.text=user.phoneNo
    }
}