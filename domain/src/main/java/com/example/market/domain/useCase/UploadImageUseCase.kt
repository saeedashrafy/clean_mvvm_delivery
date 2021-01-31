package com.example.market.domain.useCase

import com.example.market.domain.repository.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadImageUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: RequestBody, requestFile: MultipartBody.Part) = authRepository.uploadFile(name, requestFile)
}