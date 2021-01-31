package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.AppSpecificationBody
import com.example.market.domain.entity.AppSpecification

class AppSpecificationDomainToBody : Mapper<AppSpecification, AppSpecificationBody> {

    override fun invoke(domain: AppSpecification): AppSpecificationBody =
        AppSpecificationBody(
            deviceId = domain.deviceId,
            deviceModel = domain.deviceModel,
            appKey = domain.appKey,
            appVersion = domain.appVersion,
            osVersion = domain.osVersion
        )
}