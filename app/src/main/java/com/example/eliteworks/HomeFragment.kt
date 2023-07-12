package com.example.eliteworks

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eliteworks.databinding.FragmentHomeBinding

private var mUser: User? = null
class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load user data only if it hasn't been loaded before
        if (mUser == null) {
            FirestoreClass().getUserDetailsFragment(this) { user ->
                mUser = user
                Log.i("Load Data", "onCreate: Loaded Home Data 1st time")
                userDetailsSuccess(user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Display user data if it has been loaded
        mUser?.let { userDetailsSuccess(it) }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun userDetailsSuccess(user: User) {
        binding.nameUserDashboard.text = user.name
        Glide.with(this).load(user.photo).into(binding.imageUserDashboard)
    }
}
