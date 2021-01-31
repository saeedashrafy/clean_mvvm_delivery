package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.TokenResponse
import com.example.market.domain.entity.Confirm

internal class ConfirmResponseToConfirmDomain :Mapper<TokenResponse,Confirm>{
    override fun invoke(response: TokenResponse): Confirm {
        return  Confirm(response.token)
    }
}