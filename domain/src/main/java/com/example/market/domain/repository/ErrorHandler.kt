package com.example.market.domain.repository

import com.example.market.domain.entity.ErrorEntity

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}