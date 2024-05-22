package com.example.learningretrofit.data

import com.example.learningretrofit.data.model.ProductDescription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class ProductImplementation(
    private val api: Api
) : ProductsRepository {
    override suspend fun getProducts(): Flow<Result<List<ProductDescription>>> {
        return flow{
            //FLOW is a sequence of things that can happen
            val productsFromApi = try{
                api.getProductsList()
            }catch(e: IOException){
                e.printStackTrace()
                emit(Result.Error(data = null, message = "Input error"))
                return@flow
            }catch(e: HttpException){
                e.printStackTrace()
                emit(Result.Error(data = null, message = "HTTP error"))
                return@flow
            }
            catch(e: Exception){
                e.printStackTrace()
                emit(Result.Error(data = null, message = "Error Loading products"))
                return@flow
            }
            emit(Result.Success(data = productsFromApi.products))
        }
    }
}