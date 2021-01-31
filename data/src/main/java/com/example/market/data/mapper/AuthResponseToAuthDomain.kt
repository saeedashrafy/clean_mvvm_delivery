package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.AuthenticateResponse
import com.example.market.domain.entity.Authenticate

internal class AuthResponseToAuthDomain : Mapper<AuthenticateResponse, Authenticate> {
    override fun invoke(response: AuthenticateResponse): Authenticate {
        return Authenticate(hasPassword = response.hasPassword, containsKey = response.containsKey, fullName = response.fullName)
    }
}