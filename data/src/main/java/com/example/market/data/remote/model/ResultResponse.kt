package com.example.market.data.remote.model


sealed class ResultResponse<out T> {
    data class Success<T>(val data: T?) : ResultResponse<T>()
    data class Failure(val statusCode: Int?) : ResultResponse<Nothing>()
    object NetworkError : ResultResponse<Nothing>()
}
