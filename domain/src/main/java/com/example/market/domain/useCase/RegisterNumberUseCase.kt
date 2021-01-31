package com.example.market.domain.useCase


import com.example.market.domain.entity.Authenticate
import com.example.market.domain.entity.DomainResult
import com.example.market.domain.entity.Register
import com.example.market.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*

class RegisterNumberUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phoneNumber:String): Flow<DomainResult<Authenticate>> {
        return authRepository.savePhoneNumber(phoneNumber)
                .flatMapConcat { authRepository.fetchDeviceGUID() }
                .flatMapConcat { deviceId ->authRepository.registerPhoneNumber(Register(deviceId = deviceId,phoneNumber = phoneNumber))
                }
    }

}

