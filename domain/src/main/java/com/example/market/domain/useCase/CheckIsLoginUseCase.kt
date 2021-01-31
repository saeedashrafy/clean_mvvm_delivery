package com.example.market.domain.useCase

import com.example.market.domain.repository.AuthRepository

class CheckIsLoginUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke() = authRepository.isLogin()
}