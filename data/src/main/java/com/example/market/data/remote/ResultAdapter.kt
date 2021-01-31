package com.example.market.data.remote

import com.example.market.data.remote.model.ResultResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ResultAdapter(
        private val type: Type
): CallAdapter<Type, Call<ResultResponse<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<ResultResponse<Type>> = ResultCall(call)
}

class MyCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
            returnType: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                ResultResponse::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}
