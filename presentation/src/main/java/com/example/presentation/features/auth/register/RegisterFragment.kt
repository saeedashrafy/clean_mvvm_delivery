package com.example.presentation.features.auth.register

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.market.core.clicks
import com.example.market.core.textChanges
import com.example.presentation.R
import com.example.presentation.base.BaseBindingFragment
import androidx.navigation.fragment.findNavController
import com.example.market.domain.entity.AppSpecification

import com.example.presentation.databinding.FragmentRegisterBinding
import com.example.presentation.utils.snack

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


@FlowPreview
@ExperimentalCoroutinesApi
class RegisterFragment : BaseBindingFragment<FragmentRegisterBinding>(
        FragmentRegisterBinding::inflate
) {

    private val registerVM by viewModel<RegisterViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bindViewModel(registerVM)


    }


    private fun bindViewModel(registerVM: RegisterViewModel) {

        lifecycleScope.launchWhenStarted {
            registerVM.viewState
                    .onEach { render(it) }
                    .collect()
        }
        lifecycleScope.launchWhenStarted {
            registerVM.singleEvent
                    .onEach { handleEvent(it) }
                    .collect { }
        }

        intents()
                .onEach {
                    registerVM.processIntent(it)
                }
                .launchIn(lifecycleScope)
    }

    private fun handleEvent(event: SingleEvent) {
        when (event) {
            is SingleEvent.Error -> viewBinding.root.snack("خطا")
            is SingleEvent.NavigateVerify -> findNavController().navigate(R.id.navigate_to_verify)
        }
    }

    private fun intents(): Flow<ViewIntent> =
            viewBinding.run {
                merge(
                        flowOf(ViewIntent.Initial(getAppSpecification())),
                        EditTextPhoneNumber
                                .editText!!
                                .textChanges()
                                .map {
                                    ViewIntent.PhoneNumberChanged(it.toString())
                                },
                        submitBtn
                                .clicks()
                                .map {
                                    ViewIntent.Submit
                                },
                        retryButton
                                .clicks()
                                .map { ViewIntent.RetryInit(getAppSpecification()) }
                )

            }

    private fun getAppSpecification(): AppSpecification {
        val androidId = UUID.randomUUID().toString()
        val appKey = "nhwzRSV28wnjeKYQLkPfTQ3FzFu4GEu4A4PP6tNNAZGB6e7BnzzKEbcMcHnvayvP"
        val appVersion = "1.12.1"
        val osVersion = "23"
        val deviceModel = "s10"
        return AppSpecification(androidId, appKey, appVersion, osVersion, deviceModel)
    }


    private fun render(viewState: ViewState) {

        viewBinding.apply {

            var phoneNumberErrorMessage =
                    if (ValidationError.INVALID_PHONE_NUMBER in viewState.errors) {
                        getString(R.string.invalid_number_error)
                    } else {
                        null
                    }

            if (EditTextPhoneNumber.error != phoneNumberErrorMessage) {
                EditTextPhoneNumber.error = phoneNumberErrorMessage
            }

            submitBtn.isEnabled = viewState.errors.isEmpty()

            /* TransitionManager.beginDelayedTransition(
                     root,
                     AutoTransition()
                             .addTarget(progressBar)
                             .addTarget(submitBtn)
                             .setDuration(200)
             )*/


            errorGroup.isVisible = viewState.data == null && viewState.initErrors != null && !viewState.isLoading
            containerRegister.isVisible = viewState.data != null && viewState.initErrors == null  && !viewState.isLoading
            errorMessageTextView.text = viewState.initErrors?.message
            progressBarInit.isVisible = viewState.isLoading
            progressBar.isVisible = viewState.isLoading
            progressBar.isInvisible = !viewState.isLoading
            submitBtn.isInvisible = viewState.isLoading
        }
    }

}