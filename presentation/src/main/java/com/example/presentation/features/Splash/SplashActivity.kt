package com.example.presentation.features.Splash

import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.market.core.dispatchers.launchWhenStartedUntilStopped
import com.example.market.domain.entity.AppSpecification

import com.example.presentation.R
import com.example.presentation.base.BaseBindingActivity
import com.example.presentation.databinding.ActivitySplashBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.AccessController.getContext
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        bindViewModel(viewModel)

    }

    private fun bindViewModel(viewModel: SplashViewModel) {
        viewModel.stateFlow
            .onEach { }
            .launchWhenStartedUntilStopped(this)

        lifecycleScope.launchWhenStarted {

        }



    }

}