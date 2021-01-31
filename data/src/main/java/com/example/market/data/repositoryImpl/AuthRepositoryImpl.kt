package com.example.market.data.repositoryImpl

import android.util.Log
import com.example.market.core.Mapper
import com.example.market.core.dispatchers.CoroutineDispatchers
import com.example.market.data.app.AppPrefs
import com.example.market.data.remote.*
import com.example.market.data.remote.model.*
import com.example.market.data.remote.model.AuthenticateResponse
import com.example.market.data.remote.model.ConfirmBody
import com.example.market.data.remote.model.RegisterBody
import com.example.market.domain.entity.*
import com.example.market.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

internal class AuthRepositoryImpl(
        private val api: Api,
        private val dispatchers: CoroutineDispatchers,
        private val registerDomainToBody: Mapper<Register, RegisterBody>,
        private val authResponseToAuthDomain: Mapper<AuthenticateResponse, Authenticate>,
        private val appSpecificationDomainToBody: Mapper<AppSpecification, AppSpecificationBody>,
        private val appInitResponseToDomain: Mapper<AppInitResponse, AppInit>,
        private val confirmResponseToTokenDomain: Mapper<TokenResponse, Confirm>,
        private val appPrefs: AppPrefs

) : AuthRepository {

    override suspend fun registerPhoneNumber(register: Register): Flow<DomainResult<Authenticate>> {

        Log.d("TAG", register.deviceId)
        return flow {
            val res = safeCall(dispatcher = dispatchers, authResponseToAuthDomain) {
                api.authenticate(registerDomainToBody(register))
            }
            emit(res)
        }
    }


    override suspend fun verifyCode(otp: String) {


        safeCall(dispatchers, confirmResponseToTokenDomain) {
            api.token(confirmBody = ConfirmBody("phoneNumber", otp,"","",""))
        }

    }


    override suspend fun uploadFile(name: RequestBody, requestFile: MultipartBody.Part) {

        withContext(dispatchers.io) {
            val resp = api.uploadFile(requestFile)

        }
    }

    override fun initApp(appSpecification: AppSpecification): Flow<DomainResult<AppInit>> {
        return flow {
            val res = safeCall(dispatcher = dispatchers, appInitResponseToDomain) {
                (api.initialize(appSpecificationDomainToBody(appSpecification)))
            }.also {
                if (it.isRight)
                    saveDeviceGUID(appSpecification.deviceId)
            }
            emit(res)
        }
    }


    override suspend fun isLogin() =
            withContext(dispatchers.io) { appPrefs.isLogin() }


    override suspend fun saveDeviceGUID(id: String) {
        appPrefs.setDeviceId(id)
    }

    override suspend fun fetchDeviceGUID(): Flow<String> {
        return withContext(dispatchers.io) {
            appPrefs.getDeviceId()
        }
    }

    override suspend fun savePhoneNumber(phoneNumber: String): Flow<String> {
        return withContext(dispatchers.io) {
            appPrefs.setMobile(phoneNumber)
        }
    }
}