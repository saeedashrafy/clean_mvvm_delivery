package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.RegisterBody
import com.example.market.domain.entity.Register

internal class RegisterDomainToBody : Mapper<Register, RegisterBody> {

    override fun invoke(domain: Register): RegisterBody = RegisterBody(phoneNumber = domain.phoneNumber,appToken = domain.deviceId)

}