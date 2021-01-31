package com.example.market.domain.repository

import com.example.market.domain.entity.*

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface AuthRepository {
    suspend fun registerPhoneNumber(register: Register): Flow<DomainResult<Authenticate>>
    suspend fun verifyCode(otp: String)
    suspend fun uploadFile(name: RequestBody, requestFile: MultipartBody.Part)
    fun initApp(appSpecification: AppSpecification): Flow<DomainResult<AppInit>>
    suspend fun isLogin(): Flow<Boolean>
    suspend fun saveDeviceGUID(id: String)
    suspend fun savePhoneNumber(phoneNumber:String): Flow<String>
    suspend fun fetchDeviceGUID(): Flow<String>


}