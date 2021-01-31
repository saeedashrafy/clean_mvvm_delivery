package com.example.market.domain.useCase

import com.example.market.domain.entity.AppInit
import com.example.market.domain.entity.AppSpecification
import com.example.market.domain.entity.DomainResult
import com.example.market.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class InitAppUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(appSpecification: AppSpecification): Flow<DomainResult<AppInit>> {
        return authRepository.initApp(appSpecification)

    }
}