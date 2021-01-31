package com.example.presentation.features.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.base.BaseBindingFragment
import com.example.presentation.databinding.FragmentProfileBinding
import com.example.presentation.features.auth.AuthActivity


class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.buttonGoLogin.setOnClickListener {
          //  val intent=Intent(activity,AuthActivity::class.java)
          //  startActivity(intent)
        }
    }
}