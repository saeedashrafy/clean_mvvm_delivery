package com.example.market.data.remote

import com.example.market.data.remote.model.*
import com.example.market.data.remote.model.RegisterBody
import com.example.market.data.remote.model.AuthenticateResponse
import com.example.market.data.remote.model.CategoryFoodResponse
import com.example.market.data.remote.model.ConfirmBody
import okhttp3.MultipartBody
import retrofit2.http.*

internal interface Api {

    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): UploadResponse


    @Headers("Content-Type: application/json")
    @POST("App/Init")
    suspend fun initialize(@Body appSpecificationBody: AppSpecificationBody) : ResultResponse<AppInitResponse>

    @Headers("Content-Type: application/json")
    @POST("token")
    suspend fun token(@Body confirmBody: ConfirmBody) : ResultResponse<TokenResponse>

    @Headers("Content-Type: application/json")
    @POST("user/register")
    suspend fun authenticate(@Body registerBody: RegisterBody): ResultResponse<AuthenticateResponse>

    @Headers("Content-Type: application/json")
    @POST("Order/Calculate")
    suspend fun calculate(@Body cartBody:CartBody) : ResultResponse<OrderResponse>

    @GET("restaurant/menu/a3d1e25f-2c3e-4309-ae85-1f73e5bc9f9b?_=1610011121558")
    suspend fun getCategoryProducts(): ResultResponse<CategoryFoodResponse>


}
