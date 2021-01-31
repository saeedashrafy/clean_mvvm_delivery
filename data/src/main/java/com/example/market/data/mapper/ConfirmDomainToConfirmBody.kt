package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.ConfirmBody
import com.example.market.domain.entity.Confirm

internal class ConfirmDomainToConfirmBody:Mapper<Confirm, ConfirmBody>{
    override fun invoke(confirm: Confirm): ConfirmBody {
         return ConfirmBody(
             "authentication",
           "","","",""
         )
    }
}