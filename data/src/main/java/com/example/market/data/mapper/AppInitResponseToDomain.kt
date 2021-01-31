package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.AppInitResponse
import com.example.market.domain.entity.AppInit

class AppInitResponseToDomain : Mapper<AppInitResponse, AppInit> {

    override fun invoke(response: AppInitResponse): AppInit =
        AppInit(
            inReview = response.inReview,
            validLocation = response.validLocation,
            ip = response?.ip
        )

}