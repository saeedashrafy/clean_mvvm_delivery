package com.example.market.domain.useCase

import com.example.market.domain.entity.Confirm
import com.example.market.domain.repository.AuthRepository

class VerifyCodeUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(otp: String) = authRepository.verifyCode(otp)
}