package com.example.learningretrofit.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductImplementation(
    private val api: Api
) : ProductsRepository{
    override suspend fun getProducts(): Flow<Result<List<ProductDescription>>> {
        return flow{
            //FLOW is a sequence of things that can happen
        }
    }
}