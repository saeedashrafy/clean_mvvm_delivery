package com.example.presentation.features.auth.verify

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.example.market.core.clicks
import com.example.market.core.textChanges
import com.example.market.core.withLatestFrom
import com.example.presentation.R
import com.example.presentation.base.BaseBindingFragment
import com.example.presentation.databinding.FragmentVerifyBinding
import com.example.presentation.features.main.MainActivity
import com.example.presentation.utils.snack
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class VerifyFragment : BaseBindingFragment<FragmentVerifyBinding>(FragmentVerifyBinding::inflate) {

    private val verifyVM by viewModel<VerifyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel(verifyVM)

    }


    private fun bindViewModel(verifyViewModel: VerifyViewModel) {

        lifecycleScope.launchWhenStarted {

            verifyViewModel.viewState
                    .onEach {
                        updateState(it)
                    }
                    .catch { }
                    .collect()
        }
        lifecycleScope.launchWhenStarted {
            verifyViewModel.singleEvent
                    .onEach { handleSingleEvent(it) }
                    .collect()
        }

        intents().onEach {
            verifyViewModel.processIntent(it)
        }.launchIn(lifecycleScope)
    }

    private fun handleSingleEvent(it: SingleEvent) {
        when (it) {
            is SingleEvent.SubmitCodeFailure -> it.throwable.message?.let { it1 -> viewBinding.root.snack(it1) }
            is SingleEvent.SubmitCodeSuccess -> startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private fun intents(): Flow<ViewIntent> =
            with(viewBinding) {


                merge(
                        codeEditText
                                .editText!!
                                .textChanges()
                                .filter { it?.length == VerifyViewModel.MIN_LENGTH_CODE }
                                .map { ViewIntent.SubmitCode(it.toString()) },
                        retrySendCodeBtn
                                .clicks()
                                .map { ViewIntent.RetrySendCode },

                        )
            }


    private fun updateState(viewState: ViewState) {
        viewBinding.apply {
            retrySendCodeBtn.isEnabled = viewState.timer == 0
            retrySendCodeBtn.text = viewState.timer.let {
                if (viewState.timer > 0) {
                    val minute = it.div(60)
                    val seconds = it % 60
                    "$minute:$seconds"
                } else getString(R.string.retry_send)
            }

            progressBar.isInvisible = !viewState.isLoading
            submitCodeBtn.isInvisible = viewState.isLoading
        }
    }


}