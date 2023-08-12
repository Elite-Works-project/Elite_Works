package com.example.eliteworks

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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

        binding.btnSignOutProfileFragment.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnChangePasswordProfileFragment.setOnClickListener {
            startActivity(Intent(requireActivity(),ChangePasswordActivity::class.java))
        }

        return root
    }

    private fun userDetailsSuccess(user: User) {
        Glide.with(this).load(user.photo).into(binding.imageUserDashboard)
        binding.nameProfileFragment.text=user.name
        binding.phoneProfileFragment.text=user.phoneNo
    }
}