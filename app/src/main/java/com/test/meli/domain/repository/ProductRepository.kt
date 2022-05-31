package com.test.meli.domain.repository

import com.test.meli.domain.model.ResponseQuery
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProductByQuery(query: String): Flow<Result<ResponseQuery>>
}
