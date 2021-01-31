package com.example.market.data.mapper

import com.example.market.domain.entity.Failure
import com.example.market.domain.entity.ServerError

object ErrorMapper {

    fun getError(statusCode: Int?): Failure {
        return when (statusCode) {
            400 -> ServerError.AuthError.InvalidCredential
            else -> ServerError.AuthError.TokenExpired
        }
    }
}