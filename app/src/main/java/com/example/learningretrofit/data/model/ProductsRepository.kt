package com.example.learningretrofit.data.model

import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    //This will access the suspend function in the API
    //Uses abstraction
    suspend fun getProducts(): Flow<Result<List<ProductDescription>>>
}