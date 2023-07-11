package com.example.eliteworks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.eliteworks.databinding.FragmentHomeBinding
import com.example.eliteworks.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        FirestoreClass().getUserDetailsFragment(this){user -> userDetailsSuccess(user) }
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

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