package com.example.presentation.features.auth

import android.os.Bundle
import androidx.navigation.findNavController
import com.example.presentation.R
import com.example.presentation.base.BaseBindingActivity
import com.example.presentation.databinding.ActivityAuthBinding


class AuthActivity : BaseBindingActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    private val authActivityBinding by lazy { ActivityAuthBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun configureToolbar() {

    }

    override fun onBackPressed() {

        findNavController(R.id.main_content).navigateUp()
    }

}