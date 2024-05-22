package com.example.learningretrofit.data.model

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    /** Adds path to the base url
     * @Path adds the bath to the base Get call
     * For paths that can change
     * @Query is identified as a query parameter most
     * likely has a question mark
     *   suspend fun getProductsList(
     *         /**
     *          * For a mutable values in the api are passed in the parameter
     *          */
     * @Query("api_key") api_key: String,
     *      *         @Query("page") apiPage: Int,
     *      *         @Path("type") productType: String
     *
     *     ):ProductsList
     *
     */
    @GET("products/{type}")
    suspend fun getProductsList():ProductsList

    companion object{
        const val BASE_URL = "https://dummyjson.com/" // Base url is a path
    }
}