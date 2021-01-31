package com.example.market.data.remote

import com.example.market.core.Mapper
import com.example.market.core.dispatchers.CoroutineDispatchers
import com.example.market.data.mapper.ErrorMapper
import com.example.market.data.remote.model.ResultResponse
import com.example.market.domain.entity.DomainResult
import com.example.market.domain.entity.Either
import com.example.market.domain.entity.NetworkError
import kotlinx.coroutines.withContext

suspend  fun <T, R> safeCall(
    dispatcher: CoroutineDispatchers,
    transform: Mapper<T, R>,
    call : suspend () -> ResultResponse<T>
): DomainResult<R> {


    return withContext(dispatcher.io) {
        return@withContext when (val response = call.invoke()) {
            is ResultResponse.Success -> Either.Right(transform(((response.data!!))))
            is ResultResponse.Failure -> Either.Left(ErrorMapper.getError(response.statusCode))
            is ResultResponse.NetworkError -> Either.Left(NetworkError)
        }
    }
}
